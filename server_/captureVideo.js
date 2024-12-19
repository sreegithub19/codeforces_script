const { chromium } = require('playwright');  // You can use `firefox` or `webkit` as well.

(async () => {
  const browser = await chromium.launch({
    headless: true, // We need headful mode for video capture
    args: ['--no-sandbox', '--disable-setuid-sandbox']
  });
  const page = await browser.newPage();

  // Start recording video
  await page.startScreenshot({
    path: 'screenshots/video.mp4', 
    fullPage: true
  });

  // Navigate through the pages and record
  await page.goto('http://localhost:8080/hello');
  await page.waitForTimeout(1000); // Wait for 1 second

  await page.goto('http://localhost:8080/hello2');
  await page.waitForTimeout(1000); // Wait for 1 second

  await page.goto('http://localhost:8080/');
  await page.waitForTimeout(1000); // Wait for 1 second

  await browser.close();
})();
