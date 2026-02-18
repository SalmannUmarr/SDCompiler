# JFlex Setup Instructions

## Quick Start

### Windows

Use the `jflex.cmd` wrapper from `src/jflex/`:

```powershell
.\src\jflex\jflex.cmd -d src\scanner Scanner.flex
```

Or navigate to `src/jflex/` and run:

```powershell
cd src\jflex
.\jflex.cmd -d ..\scanner Scanner.flex
```

### macOS / Linux

Use the `jflex` shell script from `src/jflex/`:

```bash
./src/jflex/jflex -d src/scanner Scanner.flex
```

Or navigate to `src/jflex/` and run:

```bash
cd src/jflex
./jflex -d ../scanner Scanner.flex
```

## What's Been Set Up

All JFlex resources are now in `src/jflex/`:
- **JFlex JAR**: `src/jflex/jflex.jar` (v1.9.1)
- **Java CUP Runtime**: `src/jflex/java-cup-runtime.jar` (required by JFlex)
- **Windows Wrapper**: `src/jflex/jflex.cmd` (PowerShell-friendly)
- **Unix Wrapper**: `src/jflex/jflex` (bash-compatible)

## Common Commands

### Generate Scanner from Spec (Windows)

```powershell
.\src\jflex\jflex.cmd -d src\scanner Scanner.flex
```

### Generate Scanner from Spec (Unix/macOS)

```bash
./src/jflex/jflex -d src/scanner Scanner.flex
```

### View JFlex Version

```powershell
# Windows
.\src\jflex\jflex.cmd --version

# Unix/macOS
./src/jflex/jflex --version
```

### Get Help

```powershell
# Windows
.\src\jflex\jflex.cmd --help

# Unix/macOS
./src/jflex/jflex --help
```

## Notes

- The wrappers automatically set up the classpath with JFlex and Java CUP runtime.
- No system-wide installation is needed; everything is local to this repository.
- Both Windows `.cmd` and Unix `.sh` style scripts are provided for portability.
