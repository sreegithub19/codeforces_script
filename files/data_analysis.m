% data_analysis.m
% This script performs data analysis and plots the results

% Step 1: Generate some synthetic data
x = linspace(0, 10, 100);   % X values from 0 to 10
y = sin(x) + 0.2*randn(1, 100);  % Sine wave with noise

% Step 2: Perform basic statistical analysis
mean_y = mean(y);
std_y = std(y);
max_y = max(y);
min_y = min(y);

% Step 3: Display results
disp(['Mean of y: ', num2str(mean_y)]);
disp(['Standard Deviation of y: ', num2str(std_y)]);
disp(['Maximum of y: ', num2str(max_y)]);
disp(['Minimum of y: ', num2str(min_y)]);

% Step 4: Plot the data and statistical information
figure;
subplot(2,1,1);
plot(x, y, '-o', 'DisplayName', 'Noisy Data');
hold on;
plot(x, sin(x), '--r', 'DisplayName', 'Ideal Sine Wave');
title('Data Analysis and Plotting');
xlabel('X');
ylabel('Y');
legend('show');

% Step 5: Save the plot as an image
saveas(gcf, 'data_analysis_plot.png');

% Step 6: Save the results to a text file
resultsFile = 'data_analysis_results.txt';
fid = fopen(resultsFile, 'w');
fprintf(fid, 'Data Analysis Results\n');
fprintf(fid, '----------------------\n');
fprintf(fid, 'Mean of y: %.4f\n', mean_y);
fprintf(fid, 'Standard Deviation of y: %.4f\n', std_y);
fprintf(fid, 'Maximum of y: %.4f\n', max_y);
fprintf(fid, 'Minimum of y: %.4f\n', min_y);
fclose(fid);
