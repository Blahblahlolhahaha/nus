function skull = gsp_norm(matrix)
    gsp_matrix = gsp(matrix);
    skull = sym(normc(gsp_matrix));
end