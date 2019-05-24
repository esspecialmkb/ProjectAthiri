/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package DevFork;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Logger;

/**
 *
 * @author Michael A. Bradford <SankofaDigitalMedia.com>
 * 
 * Refactored from com.jme3.math.Vector2f;
 * 
 * @author Mark Powell
 * @author Joshua Slack
 */
public class Vector2i implements Savable, Cloneable, java.io.Serializable{
    static final long serialVersionUID = 1;
    private static final Logger logger = Logger.getLogger(Vector2i.class.getName());

    public static final Vector2i ZERO = new Vector2i(0, 0);
    public static final Vector2i UNIT_XY = new Vector2i(1, 1);
    
    //  ADDITION - CARDINAL DIRECTIONS
    public static final Vector2i NORTH = new Vector2i(0, 1);
    public static final Vector2i SOUTH = new Vector2i(0, 1);
    public static final Vector2i EAST = new Vector2i(0, 1);
    public static final Vector2i WEST = new Vector2i(0, 1);
    
    /**
     * the x value of the vector.
     */
    public int x;
    /**
     * the y value of the vector.
     */
    public int y;

    /**
     * Creates a Vector2i with the given initial x and y values.
     * 
     * @param x
     *            The x value of this Vector2i.
     * @param y
     *            The y value of this Vector2i.
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a Vector2i with x and y set to 0. Equivalent to Vector2i(0,0).
     */
    public Vector2i() {
        x = y = 0;
    }

    /**
     * Creates a new Vector2i that contains the passed vector's information
     * 
     * @param vector2f
     *            The vector to copy
     */
    public Vector2i(Vector2i vector2i) {
        this.x = vector2i.x;
        this.y = vector2i.y;
    }

    /**
     * set the x and y values of the vector
     * 
     * @param x
     *            the x value of the vector.
     * @param y
     *            the y value of the vector.
     * @return this vector
     */
    public Vector2i set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * set the x and y values of the vector from another vector
     * 
     * @param vec
     *            the vector to copy from
     * @return this vector
     */
    public Vector2i set(Vector2i vec) {
        this.x = vec.x;
        this.y = vec.y;
        return this;
    }

    /**
     * <code>add</code> adds a provided vector to this vector creating a
     * resultant vector which is returned. If the provided vector is null, null
     * is returned.
     * 
     * @param vec
     *            the vector to add to this.
     * @return the resultant vector.
     */
    public Vector2i add(Vector2i vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        return new Vector2i(x + vec.x, y + vec.y);
    }

    /**
     * <code>addLocal</code> adds a provided vector to this vector internally,
     * and returns a handle to this vector for easy chaining of calls. If the
     * provided vector is null, null is returned.
     * 
     * @param vec
     *            the vector to add to this vector.
     * @return this
     */
    public Vector2i addLocal(Vector2i vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        x += vec.x;
        y += vec.y;
        return this;
    }

    /**
     * <code>addLocal</code> adds the provided values to this vector
     * internally, and returns a handle to this vector for easy chaining of
     * calls.
     * 
     * @param addX
     *            value to add to x
     * @param addY
     *            value to add to y
     * @return this
     */
    public Vector2i addLocal(int addX, int addY) {
        x += addX;
        y += addY;
        return this;
    }

    /**
     * <code>add</code> adds this vector by <code>vec</code> and stores the
     * result in <code>result</code>.
     * 
     * @param vec
     *            The vector to add.
     * @param result
     *            The vector to store the result in.
     * @return The result vector, after adding.
     */
    public Vector2i add(Vector2i vec, Vector2i result) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        if (result == null)
            result = new Vector2i();
        result.x = x + vec.x;
        result.y = y + vec.y;
        return result;
    }

    /**
     * <code>dot</code> calculates the dot product of this vector with a
     * provided vector. If the provided vector is null, 0 is returned.
     * 
     * @param vec
     *            the vector to dot with this vector.
     * @return the resultant dot product of this vector and a given vector.
     */
    public float dot(Vector2i vec) {
        logger.warning("Floating point operation on int vector - dot product");
        if (null == vec) {
            logger.warning("Provided vector is null, 0 returned.");
            return 0;
        }
        return x * vec.x + y * vec.y;
    }

    /**
     * <code>cross</code> calculates the cross product of this vector with a
     * parameter vector v.
     * 
     * @param v
     *            the vector to take the cross product of with this.
     * @return the cross product vector.
     */
    public Vector3f cross(Vector2i v) {
        logger.warning("Floating point operation on int vector - cross product");
        return new Vector3f(0, 0, determinant(v));
    }

    public float determinant(Vector2i v) {
        logger.warning("Floating point operation on int vector - determinant");
        return (x * v.y) - (y * v.x);
    }
    
    /**
     * Sets this vector to the interpolation by changeAmnt from this to the
     * finalVec this=(1-changeAmnt)*this + changeAmnt * finalVec
     * 
     * @param finalVec
     *            The final vector to interpolate towards
     * @param changeAmnt
     *            An amount between 0.0 - 1.0 representing a percentage change
     *            from this towards finalVec
     */
    public Vector2i interpolateLocal(Vector2i finalVec, float changeAmnt) {
        logger.warning("Floating point operation on int vector - interpolation");
        this.x = (int) ((1 - changeAmnt) * this.x + changeAmnt * finalVec.x);
        this.y = (int) ((1 - changeAmnt) * this.y + changeAmnt * finalVec.y);
        return this;
    }

    /**
     * Sets this vector to the interpolation by changeAmnt from beginVec to
     * finalVec this=(1-changeAmnt)*beginVec + changeAmnt * finalVec
     * 
     * @param beginVec
     *            The begining vector (delta=0)
     * @param finalVec
     *            The final vector to interpolate towards (delta=1)
     * @param changeAmnt
     *            An amount between 0.0 - 1.0 representing a precentage change
     *            from beginVec towards finalVec
     */
    public Vector2i interpolateLocal(Vector2i beginVec, Vector2i finalVec, float changeAmnt) {
        logger.warning("Floating point operation on int vector - interpolation");
        this.x = (int) ((1 - changeAmnt) * beginVec.x + changeAmnt * finalVec.x);
        this.y = (int) ((1 - changeAmnt) * beginVec.y + changeAmnt * finalVec.y);
        return this;
    }

    /**
     * Check a vector... if it is null or its floats are NaN or infinite, return
     * false. Else return true.
     * 
     * @param vector
     *            the vector to check
     * @return true or false as stated above.
     */
    public static boolean isValidVector(Vector2i vector) {
      if (vector == null) return false;
      if (Float.isNaN(vector.x) ||
          Float.isNaN(vector.y)) return false;
      if (Float.isInfinite(vector.x) ||
          Float.isInfinite(vector.y)) return false;
      return true;
    }

    /**
     * <code>length</code> calculates the magnitude of this vector.
     * 
     * @return the length or magnitude of the vector.
     */
    public float length() {
        return FastMath.sqrt(lengthSquared());
    }

    /**
     * <code>lengthSquared</code> calculates the squared value of the
     * magnitude of the vector.
     * 
     * @return the magnitude squared of the vector.
     */
    public float lengthSquared() {
        return x * x + y * y;
    }

    /**
     * <code>distanceSquared</code> calculates the distance squared between
     * this vector and vector v.
     *
     * @param v the second vector to determine the distance squared.
     * @return the distance squared between the two vectors.
     */
    public float distanceSquared(Vector2i v) {
        double dx = x - v.x;
        double dy = y - v.y;
        return (float) (dx * dx + dy * dy);
    }

    /**
     * <code>distanceSquared</code> calculates the distance squared between
     * this vector and vector v.
     *
     * @param otherX The X coordinate of the v vector
     * @param otherY The Y coordinate of the v vector
     * @return the distance squared between the two vectors.
     */
    public float distanceSquared(float otherX, float otherY) {
        double dx = x - otherX;
        double dy = y - otherY;
        return (float) (dx * dx + dy * dy);
    }

    /**
     * <code>distance</code> calculates the distance between this vector and
     * vector v.
     *
     * @param v the second vector to determine the distance.
     * @return the distance between the two vectors.
     */
    public float distance(Vector2i v) {
        return FastMath.sqrt(distanceSquared(v));
    }

    /**
     * <code>mult</code> multiplies this vector by a scalar. The resultant
     * vector is returned.
     * 
     * @param scalar
     *            the value to multiply this vector by.
     * @return the new vector.
     */
    public Vector2i mult(float scalar) {
        logger.warning("Floating point operation on int vector - mult");
        return new Vector2i((int)(x * scalar),(int) (y * scalar));
    }

    /**
     * <code>multLocal</code> multiplies this vector by a scalar internally,
     * and returns a handle to this vector for easy chaining of calls.
     * 
     * @param scalar
     *            the value to multiply this vector by.
     * @return this
     */
    public Vector2i multLocal(float scalar) {
        logger.warning("Floating point operation on int vector - mult");
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * <code>multLocal</code> multiplies a provided vector to this vector
     * internally, and returns a handle to this vector for easy chaining of
     * calls. If the provided vector is null, null is returned.
     * 
     * @param vec
     *            the vector to mult to this vector.
     * @return this
     */
    public Vector2i multLocal(Vector2i vec) {
        logger.warning("Floating point operation on int vector - mult");
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        x *= vec.x;
        y *= vec.y;
        return this;
    }

    /**
     * Multiplies this Vector2i's x and y by the scalar and stores the result in
     * product. The result is returned for chaining. Similar to
     * product=this*scalar;
     * 
     * @param scalar
     *            The scalar to multiply by.
     * @param product
     *            The vector2f to store the result in.
     * @return product, after multiplication.
     */
    public Vector2i mult(float scalar, Vector2i product) {
        logger.warning("Floating point operation on int vector - mult");
        if (null == product) {
            product = new Vector2i();
        }

        product.x = (int) (x * scalar);
        product.y = (int) (y * scalar);
        return product;
    }

    /**
     * <code>divide</code> divides the values of this vector by a scalar and
     * returns the result. The values of this vector remain untouched.
     * 
     * @param scalar
     *            the value to divide this vectors attributes by.
     * @return the result <code>Vector</code>.
     */
    public Vector2i divide(float scalar) {
        logger.warning("Floating point operation on int vector - division");
        return new Vector2i((int)(x * scalar),(int) (y * scalar));
    }

    /**
     * <code>divideLocal</code> divides this vector by a scalar internally,
     * and returns a handle to this vector for easy chaining of calls. Dividing
     * by zero will result in an exception.
     * 
     * @param scalar
     *            the value to divides this vector by.
     * @return this
     */
    public Vector2i divideLocal(float scalar) {
        x /= scalar;
        y /= scalar;
        return this;
    }

    /**
     * <code>negate</code> returns the negative of this vector. All values are
     * negated and set to a new vector.
     * 
     * @return the negated vector.
     */
    public Vector2i negate() {
        return new Vector2i(-x, -y);
    }

    /**
     * <code>negateLocal</code> negates the internal values of this vector.
     * 
     * @return this.
     */
    public Vector2i negateLocal() {
        x = -x;
        y = -y;
        return this;
    }

    /**
     * <code>subtract</code> subtracts the values of a given vector from those
     * of this vector creating a new vector object. If the provided vector is
     * null, an exception is thrown.
     * 
     * @param vec
     *            the vector to subtract from this vector.
     * @return the result vector.
     */
    public Vector2i subtract(Vector2i vec) {
        return subtract(vec, null);
    }

    /**
     * <code>subtract</code> subtracts the values of a given vector from those
     * of this vector storing the result in the given vector object. If the
     * provided vector is null, an exception is thrown.
     * 
     * @param vec
     *            the vector to subtract from this vector.
     * @param store
     *            the vector to store the result in. It is safe for this to be
     *            the same as vec. If null, a new vector is created.
     * @return the result vector.
     */
    public Vector2i subtract(Vector2i vec, Vector2i store) {
        if (store == null)
            store = new Vector2i();
        store.x = x - vec.x;
        store.y = y - vec.y;
        return store;
    }

    /**
     * <code>subtract</code> subtracts the given x,y values from those of this
     * vector creating a new vector object.
     * 
     * @param valX
     *            value to subtract from x
     * @param valY
     *            value to subtract from y
     * @return this
     */
    public Vector2i subtract(int valX, int valY) {
        return new Vector2i(x - valX, y - valY);
    }

    /**
     * <code>subtractLocal</code> subtracts a provided vector to this vector
     * internally, and returns a handle to this vector for easy chaining of
     * calls. If the provided vector is null, null is returned.
     * 
     * @param vec
     *            the vector to subtract
     * @return this
     */
    public Vector2i subtractLocal(Vector2i vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        x -= vec.x;
        y -= vec.y;
        return this;
    }

    /**
     * <code>subtractLocal</code> subtracts the provided values from this
     * vector internally, and returns a handle to this vector for easy chaining
     * of calls.
     * 
     * @param valX
     *            value to subtract from x
     * @param valY
     *            value to subtract from y
     * @return this
     */
    public Vector2i subtractLocal(int valX, int valY) {
        x -= valX;
        y -= valY;
        return this;
    }

    /**
     * <code>normalize</code> returns the unit vector of this vector.
     * 
     * @return unit vector of this vector.
     */
    public Vector2i normalize() {
        logger.warning("Floating point operation on int vector - normalize");
        float length = length();
        if (length != 0) {
            return divide(length);
        }

        return divide(1);
    }

    /**
     * <code>normalizeLocal</code> makes this vector into a unit vector of
     * itself.
     * 
     * @return this.
     */
    public Vector2i normalizeLocal() {
        logger.warning("Floating point operation on int vector - normalize");
        float length = length();
        if (length != 0) {
            return divideLocal(length);
        }

        return divideLocal(1);
    }

    /**
     * <code>smallestAngleBetween</code> returns (in radians) the minimum
     * angle between two vectors. It is assumed that both this vector and the
     * given vector are unit vectors (iow, normalized).
     * 
     * @param otherVector
     *            a unit vector to find the angle against
     * @return the angle in radians.
     */
    public float smallestAngleBetween(Vector2i otherVector) {
        logger.warning("Floating point operation on int vector - smallest angle");
        float dotProduct = dot(otherVector);
        float angle = FastMath.acos(dotProduct);
        return angle;
    }

    /**
     * <code>angleBetween</code> returns (in radians) the angle required to
     * rotate a ray represented by this vector to lie colinear to a ray
     * described by the given vector. It is assumed that both this vector and
     * the given vector are unit vectors (iow, normalized).
     * 
     * @param otherVector
     *            the "destination" unit vector
     * @return the angle in radians.
     */
    public float angleBetween(Vector2i otherVector) {
        logger.warning("Floating point operation on int vector - angle between");
        float angle = FastMath.atan2(otherVector.y, otherVector.x)
                - FastMath.atan2(y, x);
        return angle;
    }
    
    public float getX() {
        return x;
    }

    public Vector2i setX(int x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public Vector2i setY(int y) {
        this.y = y;
        return this;
    }
    /**
     * <code>getAngle</code> returns (in radians) the angle represented by
     * this Vector2i as expressed by a conversion from rectangular coordinates (<code>x</code>,&nbsp;<code>y</code>)
     * to polar coordinates (r,&nbsp;<i>theta</i>).
     * 
     * @return the angle in radians. [-pi, pi)
     */
    public float getAngle() {
        return FastMath.atan2(y, x);
    }

    /**
     * <code>zero</code> resets this vector's data to zero internally.
     */
    public Vector2i zero() {
        x = y = 0;
        return this;
    }

    /**
     * <code>hashCode</code> returns a unique code for this vector object
     * based on it's values. If two vectors are logically equivalent, they will
     * return the same hash code value.
     * 
     * @return the hash code value of this vector.
     */
    public int hashCode() {
        int hash = 37;
        hash += 37 * hash + Float.floatToIntBits((float)x);
        hash += 37 * hash + Float.floatToIntBits((float)y);
        return hash;
    }

    @Override
    public Vector2i clone() {
        try {
            return (Vector2i) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // can not happen
        }
    }

    /**
     * Saves this Vector2i into the given float[] object.
     * 
     * @param floats
     *            The float[] to take this Vector2i. If null, a new float[2] is
     *            created.
     * @return The array, with X, Y float values in that order
     */
    public float[] toArray(float[] floats) {
        if (floats == null) {
            floats = new float[2];
        }
        floats[0] = x;
        floats[1] = y;
        return floats;
    }

    /**
     * are these two vectors the same? they are is they both have the same x and
     * y values.
     * 
     * @param o
     *            the object to compare for equality
     * @return true if they are equal
     */
    public boolean equals(Object o) {
        if (!(o instanceof Vector2i)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Vector2i comp = (Vector2i) o;
        if (Float.compare(x, comp.x) != 0)
            return false;
        if (Float.compare(y, comp.y) != 0)
            return false;
        return true;
    }

    /**
     * <code>toString</code> returns the string representation of this vector
     * object. The format of the string is such: com.jme.math.Vector2i
     * [X=XX.XXXX, Y=YY.YYYY]
     * 
     * @return the string representation of this vector.
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Used with serialization. Not to be called manually.
     * 
     * @param in
     *            ObjectInput
     * @throws IOException
     * @throws ClassNotFoundException
     * @see java.io.Externalizable
     */
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        x = in.readInt();
        y = in.readInt();
    }

    /**
     * Used with serialization. Not to be called manually.
     * 
     * @param out
     *            ObjectOutput
     * @throws IOException
     * @see java.io.Externalizable
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
    }

    public void write(JmeExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
    }

    public void read(JmeImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        x = capsule.readInt("x", 0);
        y = capsule.readInt("y", 0);
    }

    public void rotateAroundOrigin(float angle, boolean cw) {
        logger.warning("Floating point operation on int vector - rotate around origin");
        if (cw)
            angle = -angle;
        float newX = FastMath.cos(angle) * x - FastMath.sin(angle) * y;
        float newY = FastMath.sin(angle) * x + FastMath.cos(angle) * y;
        x = (int) newX;
        y = (int) newY;
    }
}
