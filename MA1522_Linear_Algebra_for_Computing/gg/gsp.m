function V = gsp(U)
  V = U(:,1);
  for u = U(:,2:end)
    for v = V 
      u = u - dot(u,v) / dot(v,v) * v
    end 
    V = [V u]
  end
end