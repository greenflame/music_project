function res = analyse(filename)
%     filename = 'y.mp3';
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
    spectrumFlux = sum(sl, 1) / size(sl, 1);
    [sfPeaksVals, sfPeaksPos] = findpeaks(spectrumFlux);
    
%     result features
    length = size(channels, 1) / frequency;
    spectrumDensity = sum(spectrum) / size(spectrum, 1);    
    sFluxPeaksDensity = size(sfPeaksVals, 2) / (length * samplesPerSecond);
    s1 = sum(spectrum(1:500)) / 500;
    s2 = sum(spectrum(500:1000)) / 500;
    s3 = sum(spectrum(1000:2000)) / 1000;

    scaleVector = [1, 1000, 1000, 1000, 1000, 1000];
    featureVector = [length, spectrumDensity, sFluxPeaksDensity, s1, s2, s3];
    
    res = featureVector.*scaleVector;
    
end