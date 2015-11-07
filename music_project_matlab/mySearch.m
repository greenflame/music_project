function result = mySearch(whereSearch, toSearch)

    for i = 1:size(whereSearch, 2)
        whereSearch(i).delta = sum(abs(whereSearch(i).spectrum - toSearch));
    end

%     result = sortStruct(whereSearch, 'delta');
    result = sortStruct(whereSearch, 'delta');
%     result = whereSearch;

end