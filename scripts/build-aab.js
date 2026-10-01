const { spawnSync } = require('node:child_process');
const path = require('node:path');
const fs = require('node:fs');

if (!fs.existsSync(path.resolve(__dirname, '../android/keystore.properties'))) {
  console.error('Release signing requires android/keystore.properties. Restore your upload key and signing settings before building.');
  process.exit(1);
}

const windows = process.platform === 'win32';
const result = spawnSync(windows ? 'gradlew.bat' : './gradlew', ['bundleRelease'], {
  cwd: path.resolve(__dirname, '../android'),
  stdio: 'inherit',
  shell: windows,
});
if (result.error) console.error(result.error.message);
process.exit(result.status ?? 1);
