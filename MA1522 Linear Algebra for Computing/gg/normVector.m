function sad = normVector(mat)
    sad = []
    for u = mat(:,1:end)
        sad = [sad 1/norm(u)*u];
    end
    
end