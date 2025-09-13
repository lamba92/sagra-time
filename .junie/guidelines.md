Sagra Time – Project Development Guidelines

Scope: This document captures project-specific knowledge to help advanced contributors build, test, and extend this repository efficiently. Keep it updated as the project evolves.

### High-level overview
Sagra Time is a Kotlin-first monorepo with multiple modules:
- `app/*`: Compose Multiplatform apps (Android/Desktop/Web) and a small web-server wrapper.
- `api-server`: JVM Ktor server with LevelDB storage and basic auth for admin endpoints.
- `core`: Shared Kotlin Multiplatform library (models, logic, shared client/server contracts).
- `crawler`: Python 3.9+ web crawler with simple `unittest` tests.
- `buildSrc/*`: Gradle convention plugins (ktlint, docker, etc.).

It uses Gradle (via the included wrapper), Kotlin Multiplatform and Compose, JUnit 5 on JVM, and Python `unittest` for the crawler. JSON serialization is via `kotlinx.serialization` across client/server.

---

### Monorepo layout (what lives where)
- Compose Multiplatform apps: `app/*`
    - `app/app-core`: Shared UI components, MVI base, Compose theme, and generated Compose resources.
        - Compose resources: `app/app-core/src/commonMain/composeResources` (enable `publicResClass`).
        - Example sources: `.../feature/cards/search/*`, `.../feature/cards/welcome/*`, theme in `SagraTimeTheme.kt`.
    - `app/app-desktop`: Desktop entry point and OS helpers.
        - Example: `app/app-desktop/src/main/kotlin/it/sagratime/app/desktop/Main.kt`.
    - `app/app-web-server`: Wrapper to serve the web app.
- Server: `api-server`
    - Ktor routes in `api-server/src/main/kotlin/it/sagratime/server/routes` (see `V1Routes.kt`, `ApiRoutes.kt`).
    - Main entry: `api-server/src/main/kotlin/it/sagratime/server/Main.kt`.
    - Uses `DB_PATH` environment variable for LevelDB location (see below).
- Shared library: `core`
    - KMP targets, shared models/contracts used by apps and server.
- Python crawler: `crawler`
    - Entry points and helpers in `crawler/src/main/python`.
    - Tests live alongside: `crawler/src/main/python/test_*.py`.
- Build logic: `buildSrc/*`
    - Convention plugins for ktlint, docker, etc.

---

1) Build and configuration instructions (project-specific)

Monorepo modules
- Android/Desktop/Web app (Compose Multiplatform): app/*
- JVM API server (Ktor): api-server
- Shared Kotlin multiplatform library: core
- Web server wrapper for the web app: app/app-web-server
- Python crawler: crawler
- Build logic and conventions: buildSrc/*

Toolchains and requirements
- Gradle: use the included Gradle wrapper (./gradlew). The project uses custom convention plugins in buildSrc.
- JDK: JVM targets require a compatible JDK (referenced via Gradle toolchains). No explicit pin here; Gradle will provision if configured.
- Kotlin Multiplatform + Compose: see compose and kotlin plugins in app modules.
- Python 3.9+ for the crawler (see crawler/requirements.txt if you need optional deps).

Known local build blocker (compose components includeBuild)
- settings.gradle.kts includes an included build path that is not present by default:
  includeBuild("compose-multiplatform/components") { ... }
- If this path does not exist locally, Gradle build/test will fail with:
  Included build '.../compose-multiplatform/components' does not exist.

Workarounds when you don’t need to modify the components sources
- Easiest: temporarily comment out or remove the includeBuild("compose-multiplatform/components") block in settings.gradle.kts for local builds. The repository already depends on published artifacts; this include is only to substitute a module during development of Compose components.
- Alternative: clone the components repo at that relative path and include the resources:library project as expected by the substitution block.
- CI note: If CI expects the includeBuild, ensure the path is present or the block is guarded by an env flag.

API server environment
- Tests and run tasks use DB_PATH environment var (set via Gradle tasks in api-server/build.gradle.kts).
  - tests: DB_PATH points to build/test-db
  - run: DB_PATH points to build/db

Compose resources
- app/app-core uses compose resources with publicResClass enabled. When adding images/fonts/strings, place them under app/app-core/src/commonMain/composeResources and update code to reference generated R-like accessors.

Docker
- api-server has a convention-docker plugin; imageName is sagra-bot-api-server. Use ./gradlew :api-server:dockerBuild (or similar task exposed by the convention) once the Gradle build is working locally.

2) Testing information

Test frameworks present
- Kotlin/JVM & Multiplatform: JUnit 5 (api-server), kotlin.test in common (core/commonTest)
- Python (crawler): unittest (standard library)

Running tests – Kotlin (when Gradle build is available)
- Root build currently fails locally unless you address the includeBuild issue (see above). After fixing it:
  - Run all tests: ./gradlew test
  - API server tests only: ./gradlew :api-server:test
    - JUnit Platform is enabled; environment DB_PATH is set automatically by the test task.
  - Core KMP tests: ./gradlew :core:allTests or specific targets, e.g., :core:jvmTest

Selecting and filtering tests (Kotlin/JUnit 5)
- Method-level filter (fully qualified): ./gradlew :api-server:test --tests "it.sagratime.server.tests.ServerTests.insertAndRetrieve"

Running tests – Python crawler
- From repo root (recommended):
  - Discover and run all crawler tests: python3 -m unittest discover -s crawler/src/main/python -p 'test_*.py' -v
  - Run a single test module: python3 -m unittest discover -s crawler/src/main/python -p 'test_post_process.py' -v
- Virtual environment (optional):
  - python3 -m venv .venv && source .venv/bin/activate
  - pip install -r crawler/requirements.txt (only if you run crawler modules that require deps)

Adding new tests – Kotlin
- API server (JUnit 5): place tests under api-server/src/test/kotlin/... and use @Test from kotlin.test or org.junit.jupiter.api.Test.
- Core (KMP): place common tests under core/src/commonTest/kotlin/...; platform-specific tests under the respective source sets.

Adding new tests – Python (crawler)
- Place tests under crawler/src/main/python with the naming pattern test_*.py so they are picked up by unittest discovery.
- Example (executed and removed during this documentation process):
  - File: crawler/src/main/python/test_smoke_demo.py
    - Content:
      import unittest
      from post_process import normalize_date
      class TestSmokeDemo(unittest.TestCase):
          def test_normalize_date_na(self):
              self.assertIsNone(normalize_date("N/A"))
      if __name__ == '__main__':
          unittest.main()
  - Run just that test via discovery: python3 -m unittest discover -s crawler/src/main/python -p 'test_smoke_demo.py' -v
  - Result: passed locally. The file was then deleted to keep the repo clean, as per these guidelines.

Notes about current test state
- The existing crawler test_post_process.py contains several assertions that currently fail for clean_name(), indicating behavior vs expectations mismatch. Run them selectively if you are working on the post_process module. This is intentional context: do not expect all crawler tests to pass until clean_name() normalization rules are aligned with tests.
- API server tests are comprehensive (Ktor TestApplication + LevelDB store). They require Gradle build to be functional (see includeBuild note).

3) Additional development information

Code style and quality
- Ktlint conventions: The build uses a custom convention-ktlint.gradle.kts in buildSrc. Use ./gradlew ktlintFormat (or the task exposed by the convention) to auto-format Kotlin code. If tasks aren’t visible, check buildSrc/src/main/kotlin/convention-ktlint.gradle.kts for exact task names.
- Keep Kotlin code multiplatform-friendly in core; avoid JVM-only APIs in commonMain.

MVI and Compose structure (app-core)
- app/app-core contains reusable Compose elements and an MVI-like ViewModel base (MVIViewModel.kt). When adding features/cards:
  - Keep state/event/effect sealed types under the feature package (see app-core/.../search and .../welcome for patterns).
  - Prefer Stateless composables + state hoisting; use remember and @Stable judiciously.
  - Compose resources: add assets under composeResources and reference via generated accessors.

Server (api-server)
- Ktor routes under api-server/src/main/kotlin/it/sagratime/server/routes. V1Routes.kt and ApiRoutes.kt define endpoints.
- DB_PATH environment is required; tests and run task set it. For manual runs: DB_PATH=/tmp/sagra-db ./gradlew :api-server:run
- Authentication: Admin endpoints require basic auth; credentials are ADMIN_USERNAME/ADMIN_PASSWORD constants in server package.

Crawler (Python)
- Entry points under crawler/src/main/python. Data model and post-processing helpers are in data_models.py and post_process.py.
- If you adjust date normalization or name cleaning, update tests accordingly and ensure locale-sensitive assumptions are explicit.

Troubleshooting & tips
- Gradle fails with missing included build: comment out includeBuild in settings.gradle.kts for local work unless you have the compose-multiplatform/components repo cloned at that path.
- Compose resource changes require regeneration; run a quick build of app-core to materialize generated classes after adding resources.
- Ktor client/server JSON uses kotlinx.serialization; ensure @Serializable data models in core remain backward-compatible across API changes.
- When touching dependency versions, update gradle/libs.versions.toml and the version catalogs (including kotlinDocumentStore from settings.gradle.kts).

Housekeeping
- local.properties is present in the repo; avoid checking in secrets. Treat it as developer-only configuration.
- Docker images: Rebuild after any API contract change so consumers get the correct server behavior.

Revision log
- 2025-09-13: Added this guidelines document. Verified Python unittest example by creating, running, and removing test_smoke_demo.py. Noted the Gradle includeBuild prerequisite for Kotlin builds/tests.
