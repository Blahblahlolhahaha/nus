function skull = lss(A, b)
    AtA = transpose(A) * A;
    Atb = transpose(A) * b;
    skull = rref([AtA Atb]);
end

