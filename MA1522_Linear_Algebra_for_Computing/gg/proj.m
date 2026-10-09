function M = proj(A, B)
    M = (dot(A, B) / norm(B) ^ 2) * B;
    M = sym(M);
end