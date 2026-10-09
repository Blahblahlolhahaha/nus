function yes = isOrtho(V)
    [~, n] = size(V);
    yes = true;
    for i = 1:n
        v = V(:,i)
        for x = i+1:n
            vv = V(:,x)
            sad = dot(v,vv)
            if sad ~= 0
                yes = false;
            end
        end
    end
end