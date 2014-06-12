from QuadraticBezier import QuadraticBezier
import math
#import matplotlib.pyplot as plt
#from matplotlib.path import Path
#import matplotlib.patches as patches
from ..kickDecider import kicks

class BezierApproach:
    def __init__(self, thePlayer):
        self.counter = 0
        self.stepIncrement = 25.
        self.player = thePlayer
        #TODO make robot location
        self.startPoint = (0, 0)
        #TODO make sweet spot
        self.endPoint = (self.player.brain.ball.rel_x, self.player.brain.ball.rel_y)
        #TODO make kick destination
        self.destinationPoint = (0., 175.)
        #TODO
        #thisKick = self.player.kick
        # if thisKick == kicks.LEFT_SIDE_KICK or thisKick == kicks.RIGHT_SIDE_KICK:
        #     rotate = True
        # else:
        #     rotate = False
        self.thisPath = QuadraticBezier(self.startPoint, None, self.endPoint, self.destinationPoint, False)
        #self.plotFullPath()

    def plotFullPath(self):
        pathPoints = [self.startPoint]
        codes = [Path.MOVETO]
        for i in xrange(1, 25):
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
        ax.set_xlim(0,500)
        ax.set_ylim(0,330)
        plt.show()

    def getNextDestinationAndHeading(self):
        timer = self.counter/self.stepIncrement
        self.dest = self.thisPath.nextPointOnCurve(timer)
        self.heading = self.thisPath.getHeading()
        self.counter = self.counter + 1
        return (self.dest[0], self.dest[1], self.heading)

    def getNextRelDestinationAndHeading(self):
        timer = self.counter/self.stepIncrement
        self.vector = self.thisPath.relVectorToNextPoint(timer)
        self.relHeading = self.thisPath.getRelHeading()
        self.counter = self.counter + 1
        return (self.vector[0], self.vector[1], self.relHeading)     

    def isAtDestination(self):
        acceptableDistance = 5.
        acceptableRelHeading = acceptableDistance/2.
        return (math.fabs(self.dest[0] - self.player.brain.loc.x) < acceptableDistance and 
        math.fabs(self.dest[1] - self.player.brain.loc.y) < acceptableDistance and 
        math.fabs(self.heading - self.player.brain.loc.h) < acceptableRelHeading)

if __name__ == "__main__":
    b = BezierApproach(None)