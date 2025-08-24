import { APP_BASE_HREF } from '@angular/common';
import { CommonEngine, isMainModule } from '@angular/ssr/node';
import express from 'express';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import bootstrap from './main.server';

const serverDistFolder = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = resolve(serverDistFolder, '../browser');
const indexHtml = join(serverDistFolder, 'index.server.html');

const app = express();
const commonEngine = new CommonEngine();

/**
 * Example Express Rest API endpoints can be defined here.
 * Uncomment and define endpoints as necessary.
 *
 * Example:
 * ```ts
 * app.get('/api/**', (req, res) => {
 *   // Handle API request
 * });
 * ```
 */

/**
 * Serve static files from /browser
 */
app.get(
  '**',
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: 'index.html'
  }),
);

/**
 * Handle all other requests by rendering the Angular application.
 */
app.get('**', (req, res, next) => {
  const { protocol, originalUrl, baseUrl, headers } = req;

  commonEngine
    .render({
      bootstrap,
      documentFilePath: indexHtml,
      url: `${protocol}://${headers.host}${originalUrl}`,
      publicPath: browserDistFolder,
      providers: [{ provide: APP_BASE_HREF, useValue: baseUrl }],
    })
    .then((html) => {
      // Inject Material theme links at SSR time based on cookie
      const themeCookie = req.headers.cookie?.match(/theme-mode=([^;]+)/)?.[1] as string | undefined;
      const LIGHT_LINK = '<link id="app-theme-link" rel="stylesheet" href="@angular/material/prebuilt-themes/azure-blue.css">';
      const DARK_LINK = '<link id="app-theme-link" rel="stylesheet" href="@angular/material/prebuilt-themes/cyan-orange.css">';
      const LIGHT_SYS = '<link id="app-theme-link-light" rel="stylesheet" href="@angular/material/prebuilt-themes/azure-blue.css" media="(prefers-color-scheme: light)">';
      const DARK_SYS = '<link id="app-theme-link-dark" rel="stylesheet" href="@angular/material/prebuilt-themes/cyan-orange.css" media="(prefers-color-scheme: dark)">';

      let inject: string;
      if (themeCookie === 'light') {
        inject = LIGHT_LINK;
      } else if (themeCookie === 'dark') {
        inject = DARK_LINK;
      } else {
        // system or undefined
        inject = LIGHT_SYS + '\n' + DARK_SYS;
      }
      const htmlOut = html.replace('</head>', `  ${inject}\n</head>`);
      res.send(htmlOut);
    })
    .catch((err) => next(err));
});

/**
 * Start the server if this module is the main entry point.
 * The server listens on the port defined by the `PORT` environment variable, or defaults to 4000.
 */
if (isMainModule(import.meta.url)) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

export default app;
