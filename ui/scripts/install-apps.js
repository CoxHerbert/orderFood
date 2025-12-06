const { spawnSync } = require('child_process');

const MIN_NPM_MAJOR = 7;

// Avoid infinite recursion if this script is triggered from nested npm installs.
if (process.env.SKIP_WORKSPACE_INSTALL === '1') {
  process.exit(0);
}

function parseNpmVersion() {
  const userAgent = process.env.npm_config_user_agent;
  const match = userAgent && userAgent.match(/npm\/(\d+)\.(\d+)\.(\d+)/);
  if (match) {
    return match.slice(1, 4).map((part) => Number(part));
  }

  const result = spawnSync('npm', ['--version'], { encoding: 'utf8' });
  if (result.status === 0 && typeof result.stdout === 'string') {
    return result.stdout.trim().split('.').map((part) => Number(part));
  }

  return null;
}

function assertWorkspaceSupport() {
  const npmVersion = parseNpmVersion();
  const npmMajor = Array.isArray(npmVersion) ? npmVersion[0] : null;

  if (!npmMajor || npmMajor < MIN_NPM_MAJOR) {
    console.error(
      `This workspace requires npm v${MIN_NPM_MAJOR}+ to install workspace dependencies. ` +
        'Please upgrade npm (e.g. `npm install -g npm@latest`) and re-run `npm install`.'
    );
    process.exit(1);
  }
}

assertWorkspaceSupport();

const installArgs = [
  'install',
  '--workspaces',
  '--include-workspace-root=false',
  '--install-strategy=linked',
];

const result = spawnSync('npm', installArgs, {
  stdio: 'inherit',
  env: { ...process.env, SKIP_WORKSPACE_INSTALL: '1' },
});

if (result.status !== 0) {
  console.error('\nWorkspace install failed. If you see "Unsupported URL Type \"workspace:\"", ' +
    'make sure you run `npm install` from the repo root (ui/) with npm v7+ so workspace links are supported.');
  process.exit(result.status ?? 1);
}
