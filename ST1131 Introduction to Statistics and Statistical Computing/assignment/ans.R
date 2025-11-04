setwd("/home/sad/Documents/nus/ST1131 Introduction to Statistics and Statistical Computing/assignment")
par(mfrow = c(2, 2))
df = read.csv("hdb_2017_2025Feb_sample.csv")

small_flats <- c("New Generation", "Model A", "Type S2", "2-room")
medium_flats <- c("Improved", "Standard", "Model A2", "Apartment", "Simplified", "Type S1")
large_flats <- c("Premium Apartment", "Adjoined flat", "Premium Apartment Loft")
executive_flats <- c("Maisonette", "Improved-Maisonette", "Model A-Maisonette",
                     "Premium Maisonette", "Terrace", "Multi Generation", "DBSS")

north <- c(
  "SEMBAWANG",
  "WOODLANDS",
  "YISHUN"
)

west <- c(
  "BUKIT BATOK",
  "BUKIT PANJANG",
  "CHOA CHU KANG",
  "JURONG EAST",
  "JURONG WEST",
  "TENGAH",
  "CLEMENTI"
)

east <- c(
  "BEDOK",
  "PASIR RIS",
  "TAMPINES",
  "GEYLANG"
)

north_east <- c(
  "HOUGANG",
  "PUNGGOL",
  "SENGKANG",
  "SERANGOON"
)

central <- c(
  "ANG MO KIO",
  "BISHAN",
  "TOA PAYOH"
)

city <- c(
  "CENTRAL AREA",
  "KALLANG/ WHAMPOA",
  "BUKIT TIMAH"
)

south <- c(
  "BUKIT MERAH",
  "QUEENSTOWN",
  "MARINE PARADE"
)

mature_estates <- c(
  "ANG MO KIO",
  "BEDOK",
  "BISHAN",
  "BUKIT MERAH",
  "BUKIT TIMAH",
  "CENTRAL AREA",
  "CLEMENTI",
  "GEYLANG",
  "KALLANG/ WHAMPOA",
  "MARINE PARADE",
  "PASIR RIS",
  "QUEENSTOWN",
  "SERANGOON",
  "TAMPINES",
  "TOA PAYOH"
)

non_mature_estates <- c(
  "BUKIT BATOK",
  "BUKIT PANJANG",
  "CHOA CHU KANG",
  "HOUGANG",
  "JURONG EAST",
  "JURONG WEST",
  "PUNGGOL",
  "SEMBAWANG",
  "SENGKANG",
  "TENGAH",
  "WOODLANDS",
  "YISHUN"
)
df$flat_type [df$flat_type == "MULTI-GENERATION"] <- "MULTI-GEN"
gae = list()
for(i in unique(df$flat_model)){gae[[length(gae) + 1]] = list(i,mean(df[df$flat_model == i,]$resale_price))}
second = sapply(sapply(gae,`[`,2),`[`,1)
sorted = order(second)
sorted_gae = gae[sorted]
flat_models = sapply(sapply(sorted_gae,`[`,1),`[`,1)

split <- strsplit(df$remaining_lease," ")
years <- as.integer(sapply(split,`[`,1))
months <- as.integer(sapply(split,`[`,3)) / 12

split_level <- strsplit(df$storey_range," ")
lvl_1 <- as.integer(sapply(split_level,`[`,1))
lvl_2 <- as.integer(sapply(split_level,`[`,3))

split_month <- strsplit(df$month,"-")
month_year <- sapply(split_month, `[`,1)
month_month <- sapply(split_month, `[`,2)

for(i in seq(1:nrow(df))){
  df$year[i] = month_year[i][1]
  df$age[i] = as.integer(month_year[i][1]) - as.integer(df$lease_commence_date[i])
  if (is.na(months[i])){
    df$remaining_lease_num[i] = years[i]
  }
  else{
    df$remaining_lease_num[i] = years[i] + months[i]
  }
  df$storey_mean[i] = (lvl_1[i] + lvl_2[i]) / 2
  df$combined_flat_type[i] = paste(df$flat_type[i],df$flat_model[i], sep="-")
  df$block_street[i] = paste(df$block[i],df$street_name[i], sep=" ")
  lease_start = as.integer(df$lease_commence_date[i])
  yeary = as.integer(df$year[i])
  df$year_rel[i] = yeary - 2017 
  if(lease_start < 1980){
    df$lease_start[i] = "< 1980"
  } else if(lease_start < 1990){
    df$lease_start[i] = "1980 - 1989"
  } else if(lease_start < 2000) {
    df$lease_start[i] = "1990 - 1999"
  } else if(lease_start < 2010) {
    df$lease_start[i] = "2000 - 2009"
  } else if(lease_start < 2020){
    df$lease_start[i] = "2010 - 2019"
  } else{
    df$lease_start[i] = "present"
  }
  if(yeary < 2021){
    df$pls_fucking_work[i] = "Before Covid"
  } else if(yeary > 2021){
    df$pls_fucking_work[i] = "After Covid"
  } else{
    df$pls_fucking_work[i] = "Covid"
  }
  df$mature[i] = as.character(df$town[i]) %in% mature_estates
  if(as.character(df$town[i]) %in% north){
    df$compass[i] = "NORTH"
  } else if(as.character(df$town[i]) %in% west){
    df$compass[i] = "WEST"
  } else if(as.character(df$town[i]) %in% east){
    df$compass[i] = "EAST"
  } else if(as.character(df$town[i]) %in% north_east){
    df$compass[i] = "NORTHEAST"
  } else if(as.character(df$town[i]) %in% south){
    df$compass[i] = "SOUTH"
  } else if(as.character(df$town[i]) %in% city){
    df$compass[i] = "CITY"
  } else{
    df$compass[i] = "CENTRAL"
  }
  
  if(df$flat_model[i] %in% small_flats){
    df$flatty[i] = "small"
  } else if(df$flat_model[i] %in% medium_flats){
    df$flatty[i] = "medium"
  } else if(df$flat_model[i] %in% large_flats){
    df$flatty[i] = "large"
  } else{
    df$flatty[i] = "exclusive"
  }
  
  df$combined_flat_type2[i] = paste(df$flat_type[i],df$flatty[i], sep="-")
}

names(df)
flat_types = c("2 ROOM","3 ROOM","4 ROOM","5 ROOM","EXECUTIVE","MULTI-GEN")
df$flat_type_num = factor(
  df$flat_type,
  levels = flat_types,
  labels=c(0,1,2,3,4,5))

df$flat_model_num = factor(
  df$flat_model,
  levels = flat_models,
  labels=c(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19))
# story range, floor_area_sqm, remaining_lease, resale_price
# story_range
hist(
  df$floor_area_sqm, 
  freq=TRUE, 
  xlim = c(30,250), 
  breaks=(250-30)/5,
  xlab = "Floor area (m^2)",
  col="lightblue",
  ylim=c(0,1100),
  main = "Histogram of Size of HDB Flats"
)

barplot(table(df$flat_type),col="lightblue")
plot(
  df$flat_type_num,
  df$floor_area_sqm, 
  x.names=flat_types,
  xlab="Flat Type",
  ylab="Floor Area in m^2",
  main="Spread of Floor Area across Flat Types",
  xaxt="n",
  yaxt = "n",
  col=c("yellow","orange","magenta","lightblue","violet",'red')
)
axis(1, at=1:6, labels=flat_types) 
ticks <- seq(min(df$floor_area_sqm),max(df$floor_area_sqm), by = 10)
axis(side = 2, at = ticks, labels=ticks)
#for (i in flat_types){
#  print(boxplot.stats(df[grepl(i,df$flat_type),7]))
#}

#df = df[!grepl("2 ROOM", df$combined_flat_type),]



plot(
  df$floor_area_sqm,df$resale_price,
  xlab="Area of Flat (sqm)",
  ylab="Resale Price of Flat",
  col=c("yellow","orange","magenta","lightblue","violet"),
  main="Scatter Plot of area of flats against resale price"
)
cov(df$floor_area_sqm,df$resale_price)

df$flat_type = as.factor(df$flat_type)
df$flat_model = as.factor(df$flat_model)
df$town = as.factor(df$town)
df$mature = as.factor(df$mature)
df$year = as.factor(df$year)
df$storey_range = as.factor(df$storey_range)
df$resale_price = log(df$resale_price)
df$lease_commence_date = as.factor(df$lease_commence_date)
df$combined_flat_type = as.factor(df$combined_flat_type)
df$lease_start = as.factor(df$lease_start)
df$pls_fucking_work = as.factor(df$pls_fucking_work)
df$compass = as.factor(df$compass)
df$street_name = as.factor(df$street_name)
df$combined_flat_type2 = as.factor(df$combined_flat_type2)

m1 = lm(formula = resale_price ~ floor_area_sqm, data=df)
m2 = lm(formula = resale_price ~ floor_area_sqm + remaining_lease_num, data=df)
m3 = lm(formula = resale_price ~ floor_area_sqm + remaining_lease_num + storey_mean, data=df)
m4 = lm(formula = resale_price ~ floor_area_sqm + storey_mean + flat_type, data=df)
m5 = lm(formula = resale_price ~ floor_area_sqm + storey_range + flat_type + town + year + remaining_lease_num + year, data=df)
m6 = lm(formula = resale_price ~ floor_area_sqm + storey_range + town + year + remaining_lease_num  + lease_commence_date + combined_flat_type, data=df)
m7 = lm(formula = resale_price ~ floor_area_sqm + town + year + age  + remaining_lease_num + combined_flat_type + storey_mean, data=df)
m8 = lm(formula = resale_price ~ floor_area_sqm + town + year + age  + remaining_lease_num + combined_flat_type + storey_mean + lease_start, data=df)
m9 = lm(formula = resale_price ~ floor_area_sqm + town + year + age  + remaining_lease_num + flat_type  + storey_mean + lease_start, data=df)
m10 = lm(formula = resale_price ~ floor_area_sqm + mature + year + age  + remaining_lease_num + flat_type  + storey_mean + lease_start, data=df)
m11 = lm(formula = resale_price ~ floor_area_sqm + mature  + remaining_lease_num + combined_flat_type  + storey_mean + lease_start + pls_fucking_work, data=df)
m12 = lm(formula = resale_price ~ floor_area_sqm + lease_start + age + combined_flat_type + remaining_lease_num   + storey_mean + pls_fucking_work + town, data=df)
m13 = lm(formula = resale_price ~ floor_area_sqm + mature + lease_start + age + combined_flat_type + remaining_lease_num   + storey_mean + pls_fucking_work + town, data=df)
m14 = lm(formula = resale_price ~ floor_area_sqm + mature + age + combined_flat_type + remaining_lease_num + pls_fucking_work + compass + storey_mean, data=df)
m15 = lm(formula = resale_price ~ floor_area_sqm + mature + age + combined_flat_type2 + pls_fucking_work + town + storey_mean, data=df)
m16 = lm(formula = resale_price ~ floor_area_sqm + mature + age + combined_flat_type2 + pls_fucking_work + town + storey_mean + year_rel, data=df)


resi_std = rstandard(m16)
hist(
  resi_std, 
  freq=TRUE, 
  xlim = c(-6,6), 
  breaks=6,
  xlab = "SR",
  col="lightblue",
  main = "SR"
)
plot(df$resale_price,resi_std)
plot(df$floor_area_sqm,resi_std)
qqnorm(resi_std)
qqline(resi_std)
C = cooks.distance(m15)
which(C>1)


