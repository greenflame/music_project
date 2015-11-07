sps = 100;

secStart = 0;
secs = 10;

filename = 'music_samples/c.mp3';

[ sg , st ] = mySpectrogram(filename, sps);

sg = sg(1:100, :);  % Freq cut

sg = sg(:, secStart * sps + 1:...
    (secStart + secs) * sps);   % Time cut

a = sum(sg) / max(sum(sg)); % Spectrum flux
a = smooth(a, 10);

figure;
plot(a);

d = diff(a);
d(1000) = 0;
d = smooth(d, 10);

% plot([a, d]);



findpeaks(d,'MinPeakHeight',0.005);
% figure;
[q, w] = findpeaks(d, 'MinPeakHeight',0.01);

sg(:, w) = 4;   % Mark peaks
figure;
imshow(mat2gray(sg));

% animation
% figure;
% peaks = w;
% peaks = peaks / sps;
% 
% [y, Fs] = audioread(filename);
% player = audioplayer(y, Fs);
% play(player);
% 
% t0 = clock;
% 
% for i = 2:size(peaks)
%     elapsed = etime(clock,t0);
%     deltaTime = peaks(i) - elapsed;
%     pause(deltaTime);
% 
%     w = 100;
%     h = 200;
%     r1 = repmat(rand(), w, h);
%     r2 = repmat(rand(), w, h);
%     r3 = repmat(rand(), w, h);
%     r = cat(3, r1, r2, r3);
%     imshow(r);
% end
% 
% ms = etime(clock,t0);
% disp(ms);
% 
% stop(player);
