from BezierMath import QuadraticBezier
import matplotlib.pyplot as plt
from matplotlib.path import Path
import matplotlib.patches as patches

class BezierApproach:
    def __init__(self):
        startPoint = (400., 50.)
        controlPoint = (25., 170.)
        endPoint = (200., 135.)
        thisPath = QuadraticBezier(startPoint, controlPoint, endPoint)
        pathPoints = [thisPath.nextPointOnCurve(0)]
        codes = [Path.MOVETO]
        for i in xrange(1, 25):
            time = i/25.
            #if robot is not moving or at destination:
            # thisPath.nextPointOnCurve(time)
            #have robot walk there
            pathPoints.append(thisPath.nextPointOnCurve(time))
            codes.append(Path.LINETO)
        codes.append(Path.LINETO)
        codes.append(Path.CLOSEPOLY)
        pathPoints.append(endPoint)
        pathPoints.append((0, 0))
        path = Path(pathPoints, codes)
        fig = plt.figure()
        ax = fig.add_subplot(111)
        patch = patches.PathPatch(path, facecolor='orange', lw=2)
        ax.add_patch(patch)
        ax.set_xlim(100,400)
        ax.set_ylim(0,300)
        plt.show()

if __name__ == "__main__":
    b = BezierApproach()

#maybe have robot recalc bezier at every destination
