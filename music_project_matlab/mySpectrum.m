function spectrum = mySpectrum(filename)

    samplesPerSecond = 10;
    windowSize = 500;

%     disp('reading file');
    [channels, frequency] = audioread(filename);

%     disp('creating spectrogram');
    s = spectrogram(channels(:, 1),...
        frequency / samplesPerSecond + windowSize, windowSize, [], frequency);

%     disp('applying log transform')
    sl = log(abs(s) + 1);
    sl(4000, 1) = 0;    % Expandind matrix
    sl = sl(1:4000, :);

    spectrum = sum(sl, 2) / size(sl, 2);

end