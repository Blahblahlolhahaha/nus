function [skull, skull_diag] = orthoDiagg(matrix, size)
    [S, skull_diag] = diagg(matrix, size);
    skull = gsp_norm(double(S));
    skull = sym(skull);
end