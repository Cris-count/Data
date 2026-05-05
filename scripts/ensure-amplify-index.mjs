import { copyFileSync, existsSync } from 'node:fs';
import { join } from 'node:path';

const browserDir = join(process.cwd(), 'dist', 'Data', 'browser');
const indexPath = join(browserDir, 'index.html');
const csrIndexPath = join(browserDir, 'index.csr.html');

if (!existsSync(indexPath) && existsSync(csrIndexPath)) {
  copyFileSync(csrIndexPath, indexPath);
  console.log('Created dist/Data/browser/index.html for static hosting.');
}
