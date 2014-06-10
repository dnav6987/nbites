import math

"""test against matplotlib curve3"""

class QuadraticBezier:
    def __init__(self, startPoint, controlPoint, endPoint):
        self.x1 = startPoint[0]
        self.x2 = controlPoint[0]
        self.x3 = endPoint[0]
        self.y1 = startPoint[1]
        self.y2 = controlPoint[1]
        self.y3 = endPoint[1]
        self.destinationX = self.x1
        self.destinationY = self.y1

    #must call in ascending time order
    def pointAtTime(self,time):
        if time >= 0 and time <= 1:
            self.destinationX = self.x1*(1-time)*(1-time) + 2*self.x2*(1-time)*time + self.x3*time*time
            self.destinationY = self.y1*(1-time)*(1-time) + 2*self.y2*(1-time)*time + self.y3*time*time
 
        else:
            print "This method must recieve a time between 0 and 1 "
        return (self.destinationX, self.destinationY)

    def setCurrentPoint(self):
        self.previousX = self.destinationX
        self.previousY = self.destinationY

    #fix
    #check difference atan vs atan2
    def setHeading(self):
        tempAngle = math.atan((self.destinationY - self.previousY)/(self.destinationX - self.previousY))
        tempAngle = math.degrees(tempAngle)
        if self.destinationY > self.previousY:
            if self.destinationX < self.previousX:
                heading = 180 + tempAngle
            else:
                heading = tempAngle
        else:
            if self.destinationX < self.previousX:
                heading = -1*180 + tempAngle
            else:
                heading = tempAngle
        print heading

    #must call in acsending time order
    def nextPointOnCurve(self, time):
        if time >= 0 and time <= 1:
            self.setCurrentPoint()
            self.pointAtTime(time)
            self.setHeading()
        else:
            print "You must input a time between 0 and 1"
        return (self.destinationX, self.destinationY)
