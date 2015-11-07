function m = folder2specs(path) % With slash

    files = rdir(strcat(path, '**/*.mp3'));

    spectrums = [];

    for i = 1:size(files, 1)
       disp(strcat('[', int2str(i), '/', int2str(size(files, 1)), '] ', files(i).name));
       
       spectrums(i).filename = files(i).name;
       spectrums(i).spectrum = mySpectrum(files(i).name);
    end

    m = spectrums;

end