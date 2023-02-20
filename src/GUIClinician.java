import java.io.IOException;
import java.util.ArrayList;

public class GUIClinician {
	private int nx;
	private int ny;
	private int nz;
	private int task;
	private int scaleFactor = 4;
	boolean startDisplay = false;
	NiftiVolume volume;
	String fileName;
	public static boolean clinicianLoadSuccess = false;

	/**
	 * @return the nx
	 */
	public int getNx() {
		return nx;
	}

	/**
	 * @return the ny
	 */
	public int getNy() {
		return ny;
	}

	/**
	 * @return the nz
	 */
	public int getNz() {
		return nz;
	}

	/**
	 * @return the task
	 */
	public int getTask() {
		return task;
	}

	/**
	 * @return the volume
	 */
	public NiftiVolume getVolume() {
		return volume;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Clinician GUI constructor that takes dimensions of the data
	 * 
	 * @param fn the nii file containing fMRI data
	 */
	public GUIClinician(String fn) {
		this.fileName = fn;
		try {
			volume = NiftiVolume.read(this.fileName);
			// 64, 64, 30, 72
			this.nx = volume.header.dim[1];
			this.ny = volume.header.dim[2];
			this.nz = volume.header.dim[3];
			this.task = volume.header.dim[4];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The convertIntensityToPixelValue method is to convert intensities in fMRI raw
	 * data to gray scale values
	 * 
	 * @param nx
	 * @param ny
	 * @param z
	 * @param t
	 * @return normalized 2D slice
	 */
	public double[][] convertIntensityToPixelValue(int nx, int ny, int z, int t) {
		double slice[][] = new double[nx][ny];
		double maxIntensity = 0;

		// Calculate maximum intensity
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				slice[x][y] = volume.data.get(x, y, z, t);
				if (slice[x][y] > maxIntensity) {
					maxIntensity = slice[x][y];
				}
			}
		}

		// Normalize data to gray scale
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				slice[x][y] = slice[x][y] / maxIntensity * 255;
			}
		}
		return slice;
	}

	/**
	 * The draw method allows to draw with the Nifti library and Penndraw the brain
	 * scans in 2D for each move expected (instruction)
	 * 
	 * @param actualMoves    the arrayList of moves expected (instructions in our
	 *                       game)
	 * @param predictedMoves the arrayList of predicted moves (actual moves in our
	 *                       game)
	 */
	public void draw(ArrayList<String> actualMoves, ArrayList<String> predictedMoves) {
		clinicianLoadSuccess = true;
		PennDraw.enableAnimation(12); // 30 / 2.5
		for (int t = 36; t < task; t++) {
			String actualLabel = actualMoves.get(t);
			String predLabel = predictedMoves.get(t);
			for (int z = 0; z < nz; z++) {

				double slice[][] = convertIntensityToPixelValue(nx, ny, z, t);

				for (int i = 0; i < nx; i++) {
					for (int j = 0; j < ny; j++) {
						int pixelValue = (int) slice[i][j];
						PennDraw.setPenColor(pixelValue, pixelValue, pixelValue);
						double xCoordinate = 1.0 / (nx) + (double) i / (nx);
						double yCoordinate = 1.0 / (ny) + (double) j / (ny);
						PennDraw.filledSquare(xCoordinate, yCoordinate, 2.0 / (nx * scaleFactor));
					}
				}
				PennDraw.setPenColor(PennDraw.WHITE);
				PennDraw.setFontSize(20);
				PennDraw.text(0.25, 0.94, "Expected: " + actualLabel);
				// if wrong, red, if correct, green
				if (actualLabel.equals(predLabel)) {
					PennDraw.setPenColor(PennDraw.GREEN);
				} else {
					PennDraw.setPenColor(PennDraw.BOOK_RED);
				}

				PennDraw.setFontSize(20);
				PennDraw.text(0.75, 0.94, "Detected: " + predLabel);

				PennDraw.advance();
			}
		}
	}
}
