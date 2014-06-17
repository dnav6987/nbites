import math

# A quadratic bezier curve is made based on the inputted points. Start and End points are required but only
# either a control point or destination point is needed. If a control point is given, then the curve will
# be the quadratic bezier dictated by the start, control, and end points. If a destination is instead given,
# a control point will be calculated such that the curve will approach the end point tangent to the line 
# connecting the endpoint and destination. A point and heading to that point at a given time (0 (start) to 1 (end)) 
# can be found as well as the next point and heading on the curve in both global and relative coordinates so
# a path can be followed
class QuadraticBezier:
    # either a controlPoint or destinationPoint must be given, but only one is needed. rotateNinety is a boolean
    # that should be true if the player wants to side kick and false otherwise
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

    # given an end point and destination, a control point for the bezier curve is calculated. The point is
    # chosen such that the curve approaches the end point tangent to the vector between the end point and
    # destination. 
    def findControlPoint(self, endPoint, destinationPoint):
        # 200 was chosen by testing numerous values and it provided a good curve for a variety of start, end, destination
        # point combinations
        DISTANCE_BETWEEN_CONTROL_AND_END = 200.
        vectorBetweenPoints = ((endPoint[0] - destinationPoint[0]), (endPoint[1] - destinationPoint[1]))
        # math.hypot(x,y) finds the distance from the origin to an (x,y) location i.e. magnitude of vector
        vectorMagnitude = math.hypot(vectorBetweenPoints[0], vectorBetweenPoints[1])
        # sometimes (for side kicks) the path should approach the endpoint orthoganol the the endpoint-destination vector
        if self.rotation == False:
            normalizedVector = (vectorBetweenPoints[0]/vectorMagnitude, vectorBetweenPoints[1]/vectorMagnitude)
        #makes the appropriate 90 degree rotation
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
        
    #this just uses the equation for a quadratic bezier curve
    #time must be between 0 and 1, 0 being the start point and 1 being the end point
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

    #TODO check again
    def setHeading(self):
        if self.destinationX - self.previousX == 0:
            if self.destinationY - self.previousY < 0:
                self.relHeading = -1.*90.
            else:
                self.relHeading = 90.
        else:
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

    #TODO CHECK
    def setRelHeading(self):
        vector = self.moveVector()
        if vector[0] == 0:
            if vector[1] < 0:
                self.relHeading = -1.*90.
            else:
                self.relHeading = 90.
        else:
            if vector[0] > 0:
                self.relHeading = math.degrees(math.atan(vector[1]/vector[0]))
            else:
                self.relHeading = -1.*(90. + math.degrees(math.atan(vector[1]/vector[0])))

    def getRelHeading(self):
        return self.relHeading

    # the vector between the last two found points on the curve
    def moveVector(self):
        changeX = self.destinationX - self.previousX
        changeY = self.destinationY - self.previousY
        #normalizer = math.sqrt(changeX*changeX + changeY*changeY)
        return (changeX, changeY)

    # TODO is this necessary???
    def setNewRelativeEndPoint():
        vector = self.moveVector()
        self.x3 = self.x3 - vector[0]
        self.y3 = self.y3 - vector[0]

    # desgined to be called in asceding time order to trace a curve
    # the smaller the change in time between calls, the more accurate the curve
    def nextPointOnCurve(self, time):
        if time >= 0 and time <= 1:
            self.setCurrentPoint()
            self.setPointAtTime(time)
            self.setHeading()
            return (self.destinationX, self.destinationY)
        else:
            print "You must input a time between 0 and 1"

    # designed to be called in ascending time order to trace a curve
    # the smaller the change in time between calls, the more accurate the curve
    def relVectorToNextPoint(self, time):
        if time >= 0 and time <= 1:
            self.setCurrentPoint()
            self.setPointAtTime(time)
            vector = self.moveVector()
            self.setRelHeading()
            self.setNewRelativeEndPoint
            return (vector[0], vector[1])
        else:
            print "You must input a time between 0 and 1"