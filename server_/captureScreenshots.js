const { chromium } = require('playwright');  // You can use `firefox` or `webkit` as well.

(async () => {
  const browser = await chromium.launch({
    headless: true, // Run in headless mode (no UI)
    args: ['--no-sandbox', '--disable-setuid-sandbox']
  });
  const page = await browser.newPage();

  // Capture /hello page
  await page.goto('http://localhost:8080/hello');
  await page.screenshot({ path: 'screenshots/hello.png' });

  // Capture /hello2 page
  await page.goto('http://localhost:8080/hello2');
  await page.screenshot({ path: 'screenshots/hello2.png' });

  // Capture the home page /
  await page.goto('http://localhost:8080/');
  await page.screenshot({ path: 'screenshots/home.png' });

  await browser.close();
})();
