import {
    ApplicationConfig, provideZoneChangeDetection, isDevMode, ENVIRONMENT_INITIALIZER,
    provideEnvironmentInitializer, inject, PLATFORM_ID
} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideClientHydration, withEventReplay} from '@angular/platform-browser';
import {provideServiceWorker} from '@angular/service-worker';
import {ThemeService} from './core/services/theme.service';
import {isPlatformBrowser} from '@angular/common';

export const appConfig: ApplicationConfig = {
    providers: [
        provideZoneChangeDetection({eventCoalescing: true}),
        provideRouter(routes),
        provideClientHydration(withEventReplay()),
        provideServiceWorker(
            'ngsw-worker.js',
            {
                enabled: !isDevMode(),
                registrationStrategy: 'registerWhenStable:30000'
            }
        ),
        provideEnvironmentInitializer(() => {
            if (isPlatformBrowser(inject(PLATFORM_ID))) inject(ThemeService);
        })
    ]
};
