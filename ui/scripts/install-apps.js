const { spawnSync } = require('child_process');

// Avoid infinite recursion if this script is triggered from nested npm installs.
if (process.env.SKIP_WORKSPACE_INSTALL === '1') {
  process.exit(0);
}

const apps = ['admin-pc', 'merchant-h5', 'user-h5'];
const env = { ...process.env, SKIP_WORKSPACE_INSTALL: '1' };

for (const app of apps) {
  const result = spawnSync('npm', ['install', '--workspace', app], {
    stdio: 'inherit',
    env,
  });

  if (result.status !== 0) {
    process.exit(result.status ?? 1);
  }
}
