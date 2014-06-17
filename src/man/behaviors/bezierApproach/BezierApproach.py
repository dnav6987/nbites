from QuadraticBezier import QuadraticBezier
import math
#import matplotlib.pyplot as plt
#from matplotlib.path import Path
#import matplotlib.patches as patches
from ..kickDecider import kicks

# this class connects the abstracted bezier math to the robots. The robot is considered the origin of the
# coordinate system, the end point is the sweet spot of the kick and the destination point is the kick
# destination. Also tells the Quadratic Bezier whether or not to rotate the for side kicks. 
class BezierApproach:
    def __init__(self, thePlayer):
        self.counter = 0
        # the number of points calculated to map the curve
        # the larger this number, the more accurate the bezier curve
        self.stepIncrement = 15.
        self.player = thePlayer
        #TODO make robot location
        self.startPoint = (0, 0)
        #TODO make sweet spot
        self.endPoint = (self.player.brain.ball.rel_x, self.player.brain.ball.rel_y)
        #TODO make kick destination
        #self.destinationPoint = (-1*self.player.brain.loc.x, 175. - self.player.brain.loc.y)
        self.destinationPoint = (-50., 50.)
        #TODO
        #thisKick = self.player.kick
        # if thisKick == kicks.LEFT_SIDE_KICK or thisKick == kicks.RIGHT_SIDE_KICK:
        #     rotate = True
        # else:
        #     rotate = False
        self.thisPath = QuadraticBezier(self.startPoint, None, self.endPoint, self.destinationPoint, False)
        #self.plotFullPath()

    # for testing. Plots the bezier curve graphically. cannot be called when using robot, only while 
    # testing directly on computer
    def plotFullPath(self):
        pathPoints = [self.startPoint]
        codes = [Path.MOVETO]
        for i in xrange(1, 20):
            time = (i+1)/self.stepIncrement
            pathPoints.append(self.thisPath.nextPointOnCurve(time))
            codes.append(Path.LINETO)
        codes.append(Path.CLOSEPOLY)
        pathPoints.append((0, 0))
        path = Path(pathPoints, codes)
        fig = plt.figure()
        ax = fig.add_subplot(111)
        patch = patches.PathPatch(path, facecolor='orange', lw=2)
        ax.add_patch(patch)
        ax.set_xlim(-10,400)
        ax.set_ylim(-100,10)
        plt.show()

    # gets the points and headings on the curve one at a time, in ascending order 
    def getNextDestinationAndHeading(self):
        self.counter = self.counter + 1
        timer = self.counter/self.stepIncrement
        self.dest = self.thisPath.nextPointOnCurve(timer)
        self.heading = self.thisPath.getHeading()
        return (self.dest[0], self.dest[1], self.heading)

    # gets the relative points and headings on the curve one at a time, in ascending order 
    def getNextRelDestinationAndHeading(self):
        self.counter = self.counter + 1
        timer = self.counter/self.stepIncrement
        self.vector = self.thisPath.relVectorToNextPoint(timer)
        self.relHeading = self.thisPath.getRelHeading()
        #TODO remove???
        if self.vector != None:
            return (self.vector[0], self.vector[1], self.relHeading)     

# makes a run script for testing on the computer (mostly used to plot paths)
if __name__ == "__main__":
    b = BezierApproach()