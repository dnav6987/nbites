import math

class QuadraticBezier:
    #either a controlPoint or destinationPoint must be given, but only 1 is needed
    def __init__(self, startPoint, controlPoint, endPoint, destinationPoint, rotateNinety):
        self.x1 = startPoint[0]
        self.x3 = endPoint[0]
        self.y1 = startPoint[1]
        self.y3 = endPoint[1]
        self.rotation = rotateNinety
        if controlPoint == None:
            calculatedControlPoint = self.findControlPoint(endPoint, destinationPoint)
            self.x2 = calculatedControlPoint[0]
            self.y2 = calculatedControlPoint[1]
        else:
            self.x2 = controlPoint[0]
            self.y2 = controlPoint[1]
        self.heading = 0.
        self.destinationX = self.x1
        self.destinationY = self.y1

    def setPointAtTime(self,time):
        if time >= 0 and time <= 1:
            self.destinationX = self.x1*(1-time)*(1-time) + 2*self.x2*(1-time)*time + self.x3*time*time
            self.destinationY = self.y1*(1-time)*(1-time) + 2*self.y2*(1-time)*time + self.y3*time*time
 
        else:
            print "This method must recieve a time between 0 and 1 "

    def getPointAtTime(self, time):
        setPointAtTime(time)
        return (self.destinationX, self.destinationY)

    def setCurrentPoint(self):
        self.previousX = self.destinationX
        self.previousY = self.destinationY

    def setHeading(self):
        tempAngle = math.atan((self.destinationY - self.previousY)/(self.destinationX - self.previousX))
        tempAngle = math.degrees(tempAngle)
        if self.destinationY > self.previousY:
            if self.destinationX < self.previousX:
                self.heading = 180 + tempAngle
            else:
                self.heading = tempAngle
        else:
            if self.destinationX < self.previousX:
                self.heading = -1*180 + tempAngle
            else:
                self.heading = tempAngle

    def getHeading(self):
        return self.heading

    def setRelHeading(self):
        vector = self.moveVector()
        if vector[0] > 0:
            self.relHeading = math.degrees(math.atan(vector[1]/vector[0]))
        else:
            self.relHeading = -1*(90 + math.degrees(math.atan(vector[1]/vector[0])))

    def getRelHeading(self):
        return self.relHeading

    def moveVector(self):
        changeX = self.destinationX - self.previousX
        changeY = self.destinationY - self.previousY
        #normalizer = math.sqrt(changeX*changeX + changeY*changeY)
        return (changeX, changeY)

    def setNewRelativeEndPoint():
        vector = self.moveVector()
        self.x3 = self.x3 - vector[0]
        self.y3 = self.y3 - vector[0]

    #must call in acsending time order
    def nextPointOnCurve(self, time):
        if time >= 0 and time <= 1:
            self.setCurrentPoint()
            self.setPointAtTime(time)
            self.setHeading()
        else:
            print "You must input a time between 0 and 1"
        return (self.destinationX, self.destinationY)

    #must call in acsending time order
    def relVectorToNextPoint(self, time):
        if time >= 0 and time <= 1:
            self.setCurrentPoint()
            self.setPointAtTime(time)
            vector = self.moveVector()
            self.setRelHeading()
            self.setNewRelativeEndPoint
        else:
            print "You must input a time between 0 and 1"
        return (vector[0], vector[1])

    def findControlPoint(self, endPoint, destinationPoint):
        DISTANCE_BETWEEN_CONTROL_AND_END = 200.
        vectorBetweenPoints = ((endPoint[0] - destinationPoint[0]), (endPoint[1] - destinationPoint[1]))
        vectorMagnitude = math.sqrt(vectorBetweenPoints[0]*vectorBetweenPoints[0] + vectorBetweenPoints[1]*vectorBetweenPoints[1])
        if self.rotation == False:
            normalizedVector = (vectorBetweenPoints[0]/vectorMagnitude, vectorBetweenPoints[1]/vectorMagnitude)
        else:
            if endPoint[1] > destinationPoint[1]:
                if endPoint[0] < destinationPoint[0]:
                    normalizedVector = (-1*vectorBetweenPoints[1]/vectorMagnitude, vectorBetweenPoints[0]/vectorMagnitude)
                else:
                    normalizedVector = (vectorBetweenPoints[1]/vectorMagnitude, -1*vectorBetweenPoints[0]/vectorMagnitude)
            else:
                if endPoint[0] < destinationPoint[0]:
                    normalizedVector = (vectorBetweenPoints[1]/vectorMagnitude, -1*vectorBetweenPoints[0]/vectorMagnitude)
                else:
                    normalizedVector = (-1*vectorBetweenPoints[1]/vectorMagnitude, vectorBetweenPoints[0]/vectorMagnitude)
        controlPoint = (endPoint[0] + DISTANCE_BETWEEN_CONTROL_AND_END*normalizedVector[0], endPoint[1] + DISTANCE_BETWEEN_CONTROL_AND_END*normalizedVector[1])
        return controlPoint