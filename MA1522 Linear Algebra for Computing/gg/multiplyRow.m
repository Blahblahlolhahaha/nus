function M = multiplyRow(matrix, factor, target)
    M = matrix;
    M(target, :) = factor * matrix(target, :);
    M = sym(M);
end

