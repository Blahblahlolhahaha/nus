function M = addRow(matrix, target, factor, source)
    M = matrix;
    M(target, :) = matrix(target, :) + factor * matrix(source, :);
    M = sym(M);
end