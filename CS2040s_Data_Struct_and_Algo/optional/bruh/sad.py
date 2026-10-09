k = 10
p = [(5,2),(3,6),(4,4)]
sad = []
for i in range(len(p)):
    sad.append([None] * 2)

def reset():
    for i in range(len(p)):
        sad[i] = [None] * 2

def solve(p: list,k: int, i: int,add):
    if i == -1:
        return (k,0,0)
    if(not None in sad[i]):
        if add:
            return sad[i][0]
        else:
            return sad[i][1]
    if i == 0:
        return (k-p[0][1],p[0][0],p[0][0])
    
    r1,h1,l1 = solve(p,k,i-1,False)
    r2,h2,l2 = solve(p,k,i-1,True)
    if not add:  
        if r2 >= p[i][1]:
            h4 = h2 + max(0,p[i][0] - l2)
            if r1 >= p[i][1]:
                h3 = h1 + max(0,p[i][0] - l1)
                if(h3 < h4):
                    ans = (r1-p[i][1],h1 + max(0,p[i][0] - l1), max(p[i][0],l1))
                    sad[i][0] = ans
                    return ans
            ans = (r2-p[i][1],h2 + max(0,p[i][0] - l2), max(p[i][0],l2))
            sad[i][0] = ans
            return ans
    h3 = h1 + p[i][0]
    h4 = h2 + p[i][0]
    if(h3 < h4):
        ans =  (k-p[i][1],h1 + p[i][0], p[i][0])
        sad[i][1] = ans
        return ans
    ans = (k-p[i][1],h2 + p[i][0], p[i][0])
    sad[i][1] = ans
    return ans
    
print(solve(p,k,p.__len__()-1,False))
reset()
# print(solve(p,k,p.__len__()-1,True))
reset()

