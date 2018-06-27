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
1. Moduł z env build arg 
2. Klasa abstrakcyjna w common i implementacja serwisu - jeśli różnie konfigurowane w różnych aplikacjach
3. Profile - jeśli różnie konfigurowane w różnych aplikacjach
4. application.properties - bean based on property goo.gl/U1rnXX, @ConditionalOnProperty
5. Primary - jeśli różnie konfigurowane w tylko jednej aplikacji
6. Order - ...

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
./gradlew dependencies | grep -i -B 10 xxx
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
Show only blank lines changes
```bash
#!/usr/bin/env bash
function main() {
    for f in `git diff --name-only`; do
        MY_DIFF=$(git diff --ignore-blank-lines ${f} | cat)
        if [[ ! ${MY_DIFF} == "" ]]; then echo "${f}"; fi
    done
}
diff <(main) <(git diff --name-only)
```
Split repositories
```bash
git filter-branch --tree-filter 'rm -rf zamowienia-server-core' --prune-empty HEAD
git for-each-ref --format="%(refname)" refs/original/ | xargs -n 1 git update-ref -d
git commit -m 'Removing zamowienia-server-core from git history'
git gc
git push origin master --force
```
```bash
git filter-branch --prune-empty --subdirectory-filter zamowienia-server-core
git remote set-url origin new-repo
git push -u origin master
```