function spectrum = mySpectrum(filename)

    samplesPerSecond = 10;
    windowSize = 500;
    lines = 4000;

    % Reading file
    [channels, frequency] = audioread(filename);

    % Creating spectrogram
    s = spectrogram(channels(:, 1),...
        frequency / samplesPerSecond + windowSize, windowSize, [], frequency);

    % Log transform
    sl = log(abs(s) + 1);
    
    % Adjusting size
    sl(lines, 1) = 0;    % Expandind matrix
    sl = sl(1:lines, :); % Narrowing down

    spectrum = sum(sl, 2) / size(sl, 2);

end