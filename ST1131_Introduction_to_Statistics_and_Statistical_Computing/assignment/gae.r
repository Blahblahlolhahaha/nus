#install.packages("dplyr")
library("dplyr")
options(scipen=3)
par(mfrow = c(2, 2))

setwd("/home/sad/Documents/nus/ST1131 Introduction to Statistics and Statistical Computing/assignment")

data = read.csv("hdb_2017_2025Feb_sample.csv")
# data

### Change all the plots to use logResalePrice

##### Exploratory Data Analysis
summary(data)

# No na / null values
cbind(lapply(
  lapply(data, is.na), sum)
)

temp <- data

## Response Variable
resalePrice_density <- density(temp$resale_price)
plot(resalePrice_density, main = "Density Plot of resale_price")
polygon(resalePrice_density, col="pink")

temp$logResalePrice <- log(temp$resale_price)

logResalePrice_density <- density(temp$logResalePrice)
plot(logResalePrice_density, main = "Density Plot of log transformed resale_price")
polygon(logResalePrice_density, col="pink")


boxplot(temp$logResalePrice, main = "Boxplot of resale_price", ylab = "Resale Price")

## Separate the year and the month values from the 'month' variable
temp$yearMonth <- data$month

timeTemp <- strsplit(temp$month, "-")
timeTemp <- matrix(unlist(timeTemp), ncol = 2, byrow = TRUE)
year <- timeTemp[,1]
month <- timeTemp[,2]

temp$year <- year
temp$month <- month

boxplot(temp$logResalePrice ~ temp$yearMonth, 
        main = "Resale Price against YearMonth", ylab = "Resale Price", xlab = "YearMonth")
boxplot(temp$logResalePrice ~ temp$year, 
        main = "Resale Price against Year", ylab = "Resale Price", xlab = "Year")
boxplot(temp$logResalePrice ~ temp$month, 
        main = "Resale Price against Month", ylab = "Resale Price", xlab = "Month")

yearLm <- lm(temp$logResalePrice ~ temp$year)
monthLm <- lm(temp$logResalePrice ~ temp$month)
summary(yearLm)
plot(yearLm)
summary(monthLm)
plot(monthLm)


## Drop 1 of 'floor_area_sqft' and 'floor_area_sqm', check which is more related
##cor(temp$logResalePrice, temp$floor_area_sqft)
#cor(temp$logResalePrice, temp$floor_area_sqm)
#cor(temp$floor_area_sqft, temp$floor_area_sqm)
# Both are correlated to each other by a value of 1, and have the same correlation with 'resale_price' at 0.8233237

floorAreaSqm_density <- density(temp$floor_area_sqm)
plot(floorAreaSqm_density, 
     main = "Density Plot of Floor Area (in sqm)", xlab = "Floor Area (in sqm)", ylab = "")
polygon(floorAreaSqm_density, col="pink")

meanByFloorArea <- temp %>%                           
  group_by(floor_area_sqm) %>%
  summarise_at(vars(logResalePrice),
               list(mean = mean)) %>% 
  as.data.frame()
with(meanByFloorArea, plot(floor_area_sqm, mean,
                           main = "Mean Resale Price against Floor Area (sqm)"), xlab = "Floor Area (sqm)", ylab = "Mean Resale Price")
abline(lm(mean ~ floor_area_sqm, data = meanByFloorArea), col = "red")


## Town
boxplot(temp$logResalePrice ~ temp$town,
        main = "Boxplot of Resale Price grouped by Town", xlab = "Town", ylab = "Resale Price")


## Break down Street Name, check no. of unique values
unique(temp$street_name)
table(temp$street_name)
jurongEast <- temp[temp$town == "JURONG EAST", ]
centralArea <- temp[temp$town == "CENTRAL AREA", ]
woodlands <- temp[temp$town == "WOODLANDS", ]

unique(jurongEast$street_name)
unique(centralArea$street_name)
unique(woodlands$street_name)

boxplot(jurongEast$logResalePrice ~ jurongEast$street_name,
        main = "Boxplot of Resale Price grouped by Street Name in Jurong East", xlab = "Street Name (Jurong East)", ylab = "Resale Price")
boxplot(centralArea$logResalePrice ~centralArea$street_name,
        main = "Boxplot of Resale Price grouped by Street Name in Central Area", xlab = "Street Name (Central Area)", ylab = "Resale Price") # Prices vary greatly for Central Area, depending on street name
boxplot(woodlands$logResalePrice ~ woodlands$street_name,
        main = "Boxplot of Resale Price grouped by Street Name in Woodlands", xlab = "Street Name (Woodlands)", ylab = "Resale Price")


### Two options: 
## 1. Group the different street types
## 2. Combine the information for Town and Street Name

groupStreetName <- function(streetName) {
  if (grepl("ST", streetName)) {
    return("STREET")
  }
  else if (grepl("DR", streetName)) {
    return("DR")
  }
  else if (grepl("AVE", streetName)) {
    return("AVE")
  }
  else if (grepl("RD", streetName)) {
    return("RD")
  }
  
  return("OTHERS")
}

woodlands$street_name <- sapply(woodlands$street_name, groupStreetName)
jurongEast$street_name <- sapply(jurongEast$street_name, groupStreetName)
centralArea$street_name <- sapply(centralArea$street_name, groupStreetName)
boxplot(woodlands$logResalePrice ~ woodlands$street_name)
boxplot(centralArea$logResalePrice ~ centralArea$street_name)
boxplot(jurongEast$logResalePrice ~ jurongEast$street_name)

## Option 1
temp$categorisedStreetName <- sapply(temp$street_name, groupStreetName)
table(temp$categorisedStreetName)
boxplot(temp$logResalePrice ~ temp$categorisedStreetName,
        main = "Boxplot of Resale Price grouped by Street Types", xlab = "Street Types", ylab = "Resale Price")

## Option 2
temp$location <- paste(temp$town, temp$categorisedStreetName, sep="-")
boxplot(temp$logResalePrice ~ temp$location,
        main = "Boxplot of Resale Price group by Location", xlab = "Location", ylab = "Resale Price")


## Flat Type
table(temp$flat_type)
boxplot(temp$logResalePrice ~ temp$flat_type,
        main = "Boxplot of Resale Price grouped by Flat Type", xlab = "Flat Type", ylab = "Resale Price")


## Flat Model
table(temp$flat_model)
boxplot(temp$logResalePrice ~ temp$flat_model,
        main = "Boxplot of Resale Price grouped by Flat Model", xlab = "Flat Model", ylab = "Resale Price")


## Lease Commence Date
cor(temp$logResalePrice, temp$lease_commence_date) ## 0.37, weak positive

meanByLeaseCommenceDate <- temp %>%                           
  group_by(lease_commence_date) %>%
  summarise_at(vars(logResalePrice),
               list(mean = mean)) %>% 
  as.data.frame()
with(meanByLeaseCommenceDate, plot(lease_commence_date, mean,
                                   main = "Mean Resale Price against Lease Commence Date", xlab = "Lease Commence Date", ylab = "Resale"))
abline(lm(mean ~ lease_commence_date, data = meanByLeaseCommenceDate), col = "red")

plot(temp$lease_commence_date, temp$logResalePrice,
     main = "Resale Price against Lease Commence Date", xlab = "Lease Commence Date", ylab = "Resale Price")
hist(temp$lease_commence_date)  ## Not a large number of data points for some years
table(temp$lease_commence_date)


## Try to group the lease_commence_date into ranges
categoriseLeaseCommenceDate <- function(leaseCommenceDate) {
  leaseCommenceDate <- as.numeric(leaseCommenceDate)
  if (leaseCommenceDate < 1980) {
    return("Before 1980s")
  }
  else if (leaseCommenceDate < 1990) {
    return("1980s")
  }
  else if (leaseCommenceDate < 2000) {
    return("1990s")
  }
  return("After 1990s")
}
temp$categorisedLeaseCommenceDate <- sapply(temp$lease_commence_date, categoriseLeaseCommenceDate)
boxplot(temp$logResalePrice ~ temp$categorisedLeaseCommenceDate,
        main = "Resale Price grouped by Lease Commence Date Range", xlab = "Lease Commence Date", ylab = "Resale Price")


## Try to see if the age of the flat is significant (year - lease_commence_date)
temp$age <- as.numeric(temp$year) - as.numeric(temp$lease_commence_date)
plot(temp$age, temp$logResalePrice,
     main = "Resale Price against Age of Flat", xlab = "Age of Flat (in years)", ylab = "Resale Price")

meanByAge <- temp %>%                           
  group_by(age) %>%
  summarise_at(vars(logResalePrice),
               list(mean = mean)) %>% 
  as.data.frame()
with(meanByAge, plot(age, mean, 
                     main = "Mean Resale Price against Age of Flat", xlab = "Age of Flat (in years)", ylab = "Mean Resale Price"))
abline(lm(mean ~ age, data = meanByAge), col = "red")

hist(temp$age,
     main = "Histogram of Age of Flat", xlab = "Age", ylab = "")
table(temp$age)
cor(temp$logResalePrice, temp$age)  ## -0.37, Weak negative



## Storey Range
boxplot(temp$logResalePrice ~ temp$storey_range,
        main = "Boxplot of Resale Price by Storey Range", xlab = "Storey Range", ylab = "Resale Price")
unique(temp$storey_range)

replaceStoreyRange <- function(storeyRange) {
  minStorey <- as.numeric(unlist(strsplit(storeyRange, " ")))[1]
  maxStorey <- as.numeric(unlist(strsplit(storeyRange, " ")))[3]
  
  if (maxStorey <= 10) {
    return("Low")
  }
  else if (maxStorey <= 21) {
    return("Middle")
  }
  return("High")
}

temp$categorisedStoreyRange <- sapply(temp$storey_range, replaceStoreyRange)
boxplot(temp$logResalePrice ~ temp$categorisedStoreyRange,
        main = "Boxplot of Resale Price grouped by Height Range of Flat", xlab = "Height Range", ylab = "Resale Price")



##### Building models
summary(temp)


### Model 1 - Location, age, floor_area_sqm, flat_type_flat_model, categorisedStoreyRange 
model1Data <- subset(temp, select = c(logResalePrice, location, age, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange))
summary(model1Data)
model1 <- lm(logResalePrice ~ ., data = model1Data)
summary(model1)
plot(model1)

model1Data[4862, ]
model1Data[4867, ]
model1Data[5010, ]
model1Data[3446, ]
model1Data[311, ]
model1Data[308, ]

## Try removing the POIs and see what effect it has on the model
testModelData <- model1Data[-c(3926, 4867, 5010, 3446, 311, 308), ]
testModel <- lm(logResalePrice ~ ., data = testModelData)
summary(testModel)
plot(testModel)
## Introduces more POIs => Likely related to the large amount of outliers


### Model 2 - Town, Street_name, age, floor_area_sqm, flat_type, flat_model, categorisedStoreyRange
model2Data <- subset(temp, select = c(logResalePrice, town, street_name, age, floor_area_sqm, flat_type, flat_model, categorisedStoreyRange))
summary(model2Data)
model2 <- lm(logResalePrice ~ ., data = model2Data)
summary(model2)
plot(model2)


### Model 3 - Town, CategorisedStreetName, age, floor_area_sqm, flat_type, flat_model, categorisedStoreyRange
model3Data <- subset(temp, select = c(logResalePrice, town, categorisedStreetName, age, floor_area_sqm, flat_type, flat_model, categorisedStoreyRange))
summary(model3Data)
model3 <- lm(logResalePrice ~ ., data = model3Data)
summary(model3)
plot(model3)


#### Trying new regressors
cor(temp$age, as.numeric(temp$year))  ## 0.1387
cor(temp$age, as.numeric(temp$month))  ## -0.033
cor(temp$age, as.numeric(temp$lease_commence_date))  ## -0.9953, do not put age and lease_commence_date together in the same model as a regressor
summary(temp$age)

categoriseFlatAge <- function(age) {
  if (age <= 15) {
    return("Young")
  }
  return("Old")
}
temp$categorisedAge <- sapply(temp$age, categoriseFlatAge)
boxplot(temp$resale_price ~ temp$categorisedAge)   ## Not statistically significant

### Model 4 - location, age, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange, yearMonth
model4Data <- subset(temp, select = c(logResalePrice, location, age, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange, yearMonth))
summary(model4Data)
model4 <- lm(logResalePrice ~ ., data = model4Data)
summary(model4)
plot(model4)

### Model 5 - location, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange, yearMonth, lease_commence_date
model5Data <- subset(temp, select = c(logResalePrice, location, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange, yearMonth, lease_commence_date))
summary(model5Data)
model5 <- lm(logResalePrice ~ ., data = model5Data)
summary(model5)
plot(model5)

### Model 6
model6Data <- subset(temp, select = c(logResalePrice, town, categorisedStreetName, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange, yearMonth, age))
summary(model6Data)
model6 <- lm(logResalePrice ~ . + (yearMonth * age) + (flat_type * flat_model), data = model6Data)
summary(model6)
plot(model6)

### Model 7
model7Data <- subset(temp, select = c(logResalePrice, town, categorisedStreetName, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange, yearMonth, age))
summary(model7Data)
model7 <- lm(logResalePrice ~ . + (yearMonth * age), data = model7Data)
summary(model7)
plot(model7)

### Model 8
model8Data <- subset(temp, select = c(logResalePrice, town, categorisedStreetName, floor_area_sqft, flat_type, flat_model, categorisedStoreyRange, yearMonth, age))
summary(model8Data)
model8 <- lm(logResalePrice ~ ., data = model8Data)
summary(model8)
plot(model8)