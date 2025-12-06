const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');

// Avoid infinite recursion if any nested npm install triggers this script.
if (process.env.SKIP_WORKSPACE_INSTALL === '1') {
  process.exit(0);
}

const ROOT = path.resolve(__dirname, '..');

function findWorkspacePackages() {
  const groups = ['packages', 'apps'];
  const workspaces = [];

  for (const group of groups) {
    const groupPath = path.join(ROOT, group);
    if (!fs.existsSync(groupPath)) continue;

    for (const entry of fs.readdirSync(groupPath, { withFileTypes: true })) {
      if (!entry.isDirectory()) continue;
      const pkgDir = path.join(groupPath, entry.name);
      const pkgJson = path.join(pkgDir, 'package.json');
      if (fs.existsSync(pkgJson)) {
        workspaces.push(pkgDir);
      }
    }
  }

  return workspaces;
}

function installWorkspace(dir) {
  const relativeDir = path.relative(ROOT, dir);
  console.log(`\nInstalling ${relativeDir}...`);

  const result = spawnSync('npm', ['install'], {
    cwd: dir,
    stdio: 'inherit',
    env: { ...process.env, SKIP_WORKSPACE_INSTALL: '1' },
  });

  if (result.status !== 0) {
    console.error(`\nFailed to install dependencies in ${relativeDir}.`);
    process.exit(result.status ?? 1);
  }
}

const workspaceDirs = findWorkspacePackages();

if (workspaceDirs.length === 0) {
  console.log('No workspace packages found under apps/ or packages/.');
  process.exit(0);
}

for (const dir of workspaceDirs) {
  installWorkspace(dir);
}
