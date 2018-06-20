# eryk-web-server-model
Common services shared across web projects.

## Important notes

- Always wait a year after beta release of common framework to be used in the project. (spring boot, spring security)
- Half a year for less significant projects (flyway, logback, webpack).
- Confgure git, .editorconfig and IntelliJ to use lf line separators
````bash
git config --global core.autocrlf input
````
- remember to wrap postgres do blocks with `begin` and `end`
- you don't need setters on model objects jackson sets value by reflection
- always sepcify method of `@RequestMapping` otherwise cors options request will invoke rest method twice

## Font
DejaVu Sans Mono 14 (or 15 on small dekstops)

## Modyfikacja bibliotek (beanów)
1. Klasa abstrakcyjna w common i implementacja serwisu - jeśli różnie konfigurowane w różnych aplikacjach
1. Profile - jeśli różnie konfigurowane w różnych aplikacjach
3. application.properties - bean based on property goo.gl/U1rnXX, @ConditionalOnProperty
2. Primary - jeśli różnie konfigurowane w tylko jednej aplikacji
3. Order - ...

### Handful scripts
Odetnij wersje
```bash
git tag -f release-xxx && git push -f --tags
```
Znajdź usunięty plik
```bash
git log --all --full-history -- */xxx.java
```
```bash
git grep xxx $(git rev-list --all)
```
Dependency tree
```bash
./gradlew app:dependencies | grep -i -B 10 xxx
```
Update last commit
```bash
git pull && git add -A && git commit --amend --no-edit  && git push -f
```
List files sorted by loc
```bash
git ls-files | xargs wc -l | sort -n -k 1 | tail -n 20
```
Delete empty directories
```bash
find . -type d -empty -delete
```