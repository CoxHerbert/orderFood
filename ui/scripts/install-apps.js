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

const npmVersion = parseNpmVersion();
const npmMajor = Array.isArray(npmVersion) ? npmVersion[0] : null;

if (!npmMajor || npmMajor < MIN_NPM_MAJOR) {
  console.error(
    `This workspace requires npm v${MIN_NPM_MAJOR}+ to install workspace dependencies. ` +
      'Please upgrade npm (e.g. `npm install -g npm@latest`) and re-run `npm install`.'
  );
  process.exit(1);
}

const result = spawnSync(
  'npm',
  ['install', '--workspaces', '--include-workspace-root=false', '--ignore-scripts'],
  {
    stdio: 'inherit',
    env: { ...process.env, SKIP_WORKSPACE_INSTALL: '1' },
  }
);

if (result.status !== 0) {
  process.exit(result.status ?? 1);
}
