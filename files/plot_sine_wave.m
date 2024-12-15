% Define the x values (from 0 to 2*pi)
x = 0:0.1:2*pi;

% Compute the corresponding y values for a sine wave
y = sin(x);

% Create the plot
figure;
plot(x, y, 'b-', 'LineWidth', 2); % 'b-' specifies blue line, LineWidth=2

% Add title and labels
title('Sine Wave');
xlabel('x');
ylabel('sin(x)');

% Save the plot as a PNG image in the output folder
saveas(gcf, '../output/plot_sine_wave.png');  % Save plot as PNG image

% Close the figure to avoid using excessive resources
close;
