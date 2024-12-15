% polynomial_fit.m
% This script fits a polynomial to noisy data and plots the result

% Step 1: Generate synthetic data with noise
x = linspace(0, 10, 100);  % X values
y = 3*x.^2 + 2*x + 1 + 5*randn(1, 100);  % Quadratic data with noise

% Step 2: Fit a polynomial of degree 2 (quadratic)
p = polyfit(x, y, 2);  % p contains the coefficients of the best fit polynomial

% Step 3: Evaluate the fitted polynomial at the data points
y_fit = polyval(p, x);

% Step 4: Plot the original data and the polynomial fit
figure;
scatter(x, y, 'b', 'DisplayName', 'Noisy Data');
hold on;
plot(x, y_fit, 'r-', 'DisplayName', 'Polynomial Fit');
title('Polynomial Fit to Noisy Data');
xlabel('X');
ylabel('Y');
legend('show');

% Step 5: Display the polynomial coefficients
disp('Polynomial Coefficients:');
disp(p);
