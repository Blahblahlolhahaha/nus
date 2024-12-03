function M = swapRow(matrix, target, source)
    M = matrix;
    M([target source], :) = matrix([source target], :);
    M = sym(M);
end

