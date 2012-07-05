/*
 *  Fractal - Java fractal generator
    Copyright (C) 2012 Luke Wallin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package fractal;

import java.awt.Point;

/**
 *
 * this interface exists to make things simpler to have both an applet and a standalone version
 * 
 * note - I'm not sure it has actually helped :/
 * 
 * @author Luke
 */
public interface IFractalWindow {
    public void repaint();
    public Point getMousePosition(boolean relative);
    //how much has been generated?
//    public void generating(double progress);
//    public void finishedGenerating();
    public void saving(int progress);
}
