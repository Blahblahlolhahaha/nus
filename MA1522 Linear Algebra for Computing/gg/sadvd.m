function [saddd sadd sad] = sadvd(matrix)
    test = transpose(sym(matrix)) * matrix
    values = sort(eig(test),"descend");
    [m n] = size(test);
    [saddd ,sadd] = diagg(sym(test),n)
    for i = 1:n
        saddd(:,i) = 1/(norm(saddd(:,i))) * saddd(:,i);
    end
    sadd = sym(sadd)^0.5;
    sad = [];
    for i = 1:n
        if sadd(i,i) ~= 0
            sad = [sad (1/sadd(i,i))*matrix*sym(saddd(:,i))];
        end
    end
    [mm nn] = size(sad);
    if mm ~= m
        sad = simplify([sad null(transpose(sad))]);
    end
end