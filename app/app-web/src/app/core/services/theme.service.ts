import { Injectable, Inject, PLATFORM_ID, signal, effect } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

/**
 * ThemeMode enumerates the available theme selection strategies for the app.
 * - System: Follow the operating system's color scheme via prefers-color-scheme.
 * - Light: Force the light theme (azure-blue prebuilt Material theme).
 * - Dark: Force the dark theme (cyan-orange prebuilt Material theme).
 */
export enum ThemeMode {
  System = 'system',
  Light = 'light',
  Dark = 'dark',
}

/**
 * ThemeService centrally manages Angular Material prebuilt theme loading at runtime.
 *
 * Why a service instead of static styles?
 * - Prebuilt themes are CSS (not Sass mixins), so dynamic selection must occur at runtime.
 * - We need to support three modes (light, dark, system) and allow switching without reloads.
 * - Using link elements keeps bundle size stable and avoids double-loading themes unnecessarily.
 *
 * SSR/browser considerations:
 * - All DOM operations are guarded by isPlatformBrowser because the app may render on the server.
 * - On the server, the service avoids DOM access; the actual theme gets applied in the browser.
 *
 * How it works:
 * - Maintains a reactive mode signal and a derived isDark signal.
 * - In System mode, it inserts two <link rel="stylesheet"> tags with media queries so the browser
 *   chooses and live-switches the theme when the OS preference changes.
 * - In Light/Dark mode, it uses a single managed link with a direct href and no media attribute to
 *   avoid duplicate downloads and to ensure a deterministic theme.
 * - The chosen mode is persisted in localStorage.
 */
@Injectable({ providedIn: 'root' })
export class ThemeService {
  /** LocalStorage key under which the user's theme mode preference is saved. */
  private readonly storageKey = 'theme-mode';
  /** Href for the Material light theme (azure-blue). */
  private readonly lightHref = '@angular/material/prebuilt-themes/azure-blue.css';
  /** Href for the Material dark theme (cyan-orange). */
  private readonly darkHref = '@angular/material/prebuilt-themes/cyan-orange.css';

  /**
   * Current theme mode preference as a writable Angular signal.
   * Consumers can subscribe to mode() or call setMode/getMode to control the theme.
   */
  public readonly mode = signal<ThemeMode>(ThemeMode.System);

  /**
   * Effective boolean indicating whether dark styling should be applied.
   * In System mode this derives from window.matchMedia('(prefers-color-scheme: dark)').
   */
  private readonly isDark = signal<boolean>(false);

  /** Media query list used to react to system dark mode changes. */
  private mediaQuery?: MediaQueryList;
  /** The single managed link element used in Light/Dark modes. */
  private linkEl?: HTMLLinkElement; // the managed theme link element

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    if (isPlatformBrowser(this.platformId)) {
      // Initialize from storage or default to System mode.
      const saved = (localStorage.getItem(this.storageKey) as ThemeMode | null) ?? ThemeMode.System;
      this.mode.set(saved);

      this.setupMediaQuery();
      this.updateIsDark();
      // Apply without creating a single link in System mode to reuse SSR-injected links
      this.applyTheme();

      // React to mode changes automatically. This persists the selection and reapplies the theme.
      effect(() => {
        const m = this.mode();
        if (isPlatformBrowser(this.platformId)) {
          try { localStorage.setItem(this.storageKey, m); } catch {}
          try {
            const maxAge = 60 * 60 * 24 * 365; // 1 year
            document.cookie = `theme-mode=${m}; Path=/; Max-Age=${maxAge}`;
          } catch {}
        }
        this.updateIsDark();
        this.applyTheme();
      });
    }
  }

  /**
   * Prepare the matchMedia listener for system dark mode changes.
   * Why: In System mode, we need to track OS changes to update theme immediately without reload.
   */
  private setupMediaQuery() {
    if (!isPlatformBrowser(this.platformId)) return;
    this.mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    const listener = () => {
      if (this.mode() === ThemeMode.System) {
        this.updateIsDark();
        this.applyTheme();
      }
    };
    // Modern addEventListener is preferred, but fall back for Safari <=13
    try { this.mediaQuery.addEventListener('change', listener); }
    catch {
        try {this.mediaQuery.addListener(listener); }
        catch { console.warn('Failed to set up system theme listener'); }
    }
  }

  /**
   * Update the derived isDark signal based on the current mode and system preference.
   * Why: Components can use isDark for conditional UI without duplicating the logic here.
   */
  private updateIsDark() {
    const mode = this.mode();
    if (mode === ThemeMode.Dark) { this.isDark.set(true); return; }
    if (mode === ThemeMode.Light) { this.isDark.set(false); return; }
    const systemDark = this.mediaQuery?.matches ?? false;
    this.isDark.set(systemDark);
  }

  /**
   * Ensure there is a single managed <link> tag to host the theme in forced modes.
   * Why: Avoid repeatedly creating elements and keep predictable DOM shape.
   */
  private ensureLinkElement() {
    if (!isPlatformBrowser(this.platformId)) return;
    const id = 'app-theme-link';
    const existing = document.getElementById(id) as HTMLLinkElement | null;
    if (existing) { this.linkEl = existing; return; }
    const link = document.createElement('link');
    link.id = id;
    link.rel = 'stylesheet';
    // Keep it after Google Fonts for proper cascade
    document.head.appendChild(link);
    this.linkEl = link;
  }

  /**
   * Apply the theme according to the current mode and effective darkness.
   * Why: Centralizes the DOM updates (href, media, link creation/cleanup) for consistency.
   */
  private applyTheme() {
    if (!isPlatformBrowser(this.platformId)) return;
    const isDark = this.isDark();

    // When following system, we can use media attribute to let browser switch automatically
    if (this.mode() === ThemeMode.System) {
      // Load both via two link elements for instant system switching
      this.setupSystemLinks();
      return;
    }

    // If forcing a mode, ensure only one link with direct href and no media
    // Remove any system pair if present
    this.cleanupSystemLinks();

    // ensure single link exists
    if (!this.linkEl) this.ensureLinkElement();
    if (!this.linkEl) return;

    this.linkEl.media = '';
    this.linkEl.href = isDark ? this.darkHref : this.lightHref;
  }

  /**
   * Set up the pair of system-following link elements (one for light, one for dark) with media queries.
   * Why: This allows the browser to automatically pick the correct theme and live-switch on OS changes.
   */
  private setupSystemLinks() {
    if (!isPlatformBrowser(this.platformId)) return;
    // Ensure we have two links: one light with media light, one dark with media dark.
    const lightId = 'app-theme-link-light';
    const darkId = 'app-theme-link-dark';

    // Remove single link if exists
    if (this.linkEl?.id === 'app-theme-link') {
      this.linkEl.remove();
      this.linkEl = undefined;
    }

    let light = document.getElementById(lightId) as HTMLLinkElement | null;
    let dark = document.getElementById(darkId) as HTMLLinkElement | null;

    if (!light) {
      light = document.createElement('link');
      light.id = lightId;
      light.rel = 'stylesheet';
      light.media = '(prefers-color-scheme: light)';
      light.href = this.lightHref;
      document.head.appendChild(light);
    }
    if (!dark) {
      dark = document.createElement('link');
      dark.id = darkId;
      dark.rel = 'stylesheet';
      dark.media = '(prefers-color-scheme: dark)';
      dark.href = this.darkHref;
      document.head.appendChild(dark);
    }
  }

  /**
   * Remove the system-following link pair if present.
   * Why: Prevent conflicts and unnecessary duplicates when switching to forced Light/Dark modes.
   */
  private cleanupSystemLinks() {
    const light = document.getElementById('app-theme-link-light');
    const dark = document.getElementById('app-theme-link-dark');
    if (light) light.remove();
    if (dark) dark.remove();
  }
}
