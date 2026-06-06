hawker_ctr_raw <- list(list(2, list(STREETNAME = "Commonwealth Drive", POSTALCODE = "141001"), list(list(STREETNAME = "Kensington Park Road", POSTALCODE = "557269"))))

hawker_ctr_raw[[1]][[1]]
hawker_ctr_raw[[1]][[2]]
hawker_ctr_raw[[1]][[3]]


stud = read.csv("pyp/2420/student-mat.csv",sep=";")


lines(density(stud[, "G3"]))

hist(stud[, "G3"], probability = TRUE, xlim = range(-10, 25))
x <- stud[, "G3"]
x_ = c(x,-x)
g3_density = density(x_,bw = 1.5)
x = g3_density$x[g3_density$x > 0]
y = 2*g3_density$y[g3_density$x > 0]
lines(x,y)
