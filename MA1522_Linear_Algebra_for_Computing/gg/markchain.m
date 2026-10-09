function sad = markchain(matrix, n, start)
    for v = matrix(:,1:end)
        if sum(v) ~= 1
            disp("Not 1")
            return
        end
    end
    [x y] = size(matrix);
    [A D] = diagg(matrix,y)
    sad = A * D ^ n * inv(A) * start;
end