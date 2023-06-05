package envision.engine.rendering;

public enum GLModes {
	
	/** Draw a series of unconnected points. */
	POINTS,
	/** Draw a series of unconnected lines. */
	LINES,
	/** Draw a series of connected lines. Specify line points from tail to tip. */
	LINE_STRIP,
	/** Similar to line strip, except the very first and last point will also be connected. */
	LINE_LOOP,
	/** Draw a series of unconnected triangles. */
	TRIANGLES,
	/** Draw a series of connected triangles where the next declared point will form a new triangle from the previous two points. */
	TRIANGLE_STRIP,
	/** Draw a series of connected triangles where each triangle will share the first point. */
	TRIANGLE_FAN,
	/** Draws a series of unconnected rectangles. */
	QUADS,
	/** Draws a series of connected rectangles. */
	QUAD_STRIP;
	
}
