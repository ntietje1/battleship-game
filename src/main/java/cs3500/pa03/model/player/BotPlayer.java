package cs3500.pa03.model.player;


import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.ShipType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents an AI player in a battleship game.
 * Uses a smart shooting algorithm
 */
public class BotPlayer extends AbstractPlayer {

  private List<Posn> posnsToAttack = new ArrayList<>();
  private List<Posn> currentVolley = new ArrayList<>();
  private List<Posn> lastFinderPosns = new ArrayList<>();
  private int shotsToFire;

  /**
   * constructor for a BotPlayer
   */
  public BotPlayer() {
    super();
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "auraHerce";
  }

  /**
   * Fire a volley of shots, whose size is equal to the number of this player's alive ships
   *
   * @return a list of Posns to fire on
   */
  @Override
  public List<Posn> takeShots() {
    this.currentVolley.clear();
    this.shotsToFire =
        Integer.min(this.friendlyBoard.getAliveShipCount(), this.enemyBoard.countAvailableCells());
    System.out.println(this.name() + " has " + this.shotsToFire + " shots to fire");

    // Use remaining shots to fire using AI
    this.smartShoot();
    this.lastVolleyFired = currentVolley;

    System.out.println("Firing :" + this.currentVolley.size() + " shots");
    for (Posn posn : currentVolley) {
      this.enemyBoard.attack(posn);
      System.out.println(this.name() + " fired at: " + posn);
    }
    return currentVolley;
  }

  /**
   * If there are no ships of known location, find them through regularly spaced shots.
   * Otherwise, attempt to efficiently destroy ships in as few shots as possible.
   * Delegates shooting logic to one of three "modes"
   * Prioritizes destroying ships, then finding directions of known ships, then finding new ships
   * Modifies currentVolley and shotsToFire
   */
  private void smartShoot() {

    this.fireAtSingleHits();

    this.destroyShips();

    this.findShipDirection();

    this.shipFinder();
  }

  /**
   * fires a shot at a target and its adjacent cells
   */
  private void fireAtSingleHits() {
    for (int x = 0; x < enemyBoard.getWidth(); x++) {
      for (int y = 0; y < enemyBoard.getHeight(); y++) {
        Posn targetPosn = new Posn(x, y);
        if (!enemyBoard.checkHit(targetPosn)) {
          break;
        }
        boolean up =
            !enemyBoard.isValidPosn(new Posn(x, y - 1)) || enemyBoard.checkHit(new Posn(x, y - 1));
        boolean down =
            !enemyBoard.isValidPosn(new Posn(x, y + 1)) || enemyBoard.checkHit(new Posn(x, y + 1));
        boolean left =
            !enemyBoard.isValidPosn(new Posn(x - 1, y)) || enemyBoard.checkHit(new Posn(x - 1, y));
        boolean right =
            !enemyBoard.isValidPosn(new Posn(x + 1, y)) || enemyBoard.checkHit(new Posn(x + 1, y));
        boolean hamburger = up && down;
        boolean hotdog = left && right;
        if (hamburger || hotdog) {
          List<Posn> shots = getValidAdjacentCells(targetPosn);
          this.posnsToAttack.addAll(shots);
        }
      }
    }
  }

  /**
   * Continue shooting at possible ship locations until a miss
   * Evenly splits shots between possible ship locations
   * Modifies currentVolley
   */
  private void destroyShips() {
    // for every >=2 hits in a row, continue firing in that direction until a miss
    int iterations = 0;
    int shotsAdded = 0;
    int totalShotsAdded = 0;
    outer:
    do {
      for (int x = 0; x < enemyBoard.getWidth(); x++) {
        for (int y = 0; y < enemyBoard.getHeight(); y++) {
          Posn posn = new Posn(x, y);
          if (isPossibleContinuation(posn) && isValidCell(posn)) {
            shotsAdded++;
            totalShotsAdded++;
            this.currentVolley.add(posn);
            this.shotsToFire--;
            if (shotsToFire == 0) {
              break outer;
            }
          }
        }
      }
      iterations++;
    } while (shotsAdded > 0 && iterations <= 2);
    System.out.println("Fired " + totalShotsAdded + " shots to destroy ships!");
  }


  /**
   * Does this posn continue a series of two or more hits?
   *
   * @param posn position to originate search from
   * @return true if the posn does continue a series of two or more hits
   */
  private boolean isPossibleContinuation(Posn posn) {
    // if the next two positions in any direction are hits, fire at this position
    // Check four adjacent cells and add them to the list if they are valid
    return !enemyBoard.checkAttacked(posn)
        && (andMapIsHit(posn.offsetX(2)) || andMapIsHit(posn.offsetX(-2))
        || andMapIsHit(posn.offsetY(2)) || andMapIsHit(posn.offsetY(-2)));
  }

  /**
   * for every last successful hit, fire in all 4 directions adjacent to
   * the hit that aren't already attacked
   * If there are more positions to be attacked this way than shotsToFire,
   * add the rest to posnsToAttack
   */
  private void findShipDirection() {
    ArrayList<Posn> shots = new ArrayList<>();

    // Fire at stored posns first
    while (shotsToFire > 0 && this.posnsToAttack.size() > 0) {
      Posn posn = posnsToAttack.remove(0);
      if (isValidCell(posn)) {
        currentVolley.add(posn);
        shotsToFire--;
      }

    }
    System.out.println("Fired " + currentVolley.size() + " STORED shots to find continuations!");

    // Add all valid adjacent cells to hit list
    for (Posn hit : this.lastSuccessfulHits) {
      for (Posn adjacentPosn : getValidAdjacentCells(hit)) {
        if (!shots.contains(adjacentPosn) && lastFinderPosns.contains(hit)) {
          shots.add(adjacentPosn);
        }
      }
    }

    // If there are more positions to be attacked this way than shotsToFire,
    // store the rest in posnsToAttack (will be used in future volleys)
    while (!shots.isEmpty() && shots.size() > shotsToFire) {
      this.posnsToAttack.add(shots.remove(0));
    }

    this.lastFinderPosns.clear();
    this.currentVolley.addAll(shots);
    shotsToFire -= shots.size();
    System.out.println("Fired " + shots.size() + " shots to find continuations!");
  }

  /**
   * Get all adjacent positions that could continue a ship
   *
   * @param posn position to originate search from
   * @return list of positions to fire at
   */
  private List<Posn> getValidAdjacentCells(Posn posn) {
    List<Posn> validAdjacentCells = new ArrayList<>();

    for (Posn adjacentPosn : getAdjacentPosns(posn)) {
      if (isValidCell(adjacentPosn)) {
        validAdjacentCells.add(adjacentPosn);
      }
    }

    return validAdjacentCells;
  }

  /**
   * Count the number of valid cells in the given list of posns
   *
   * @param posns posns to check
   * @return number of valid posns
   */
  private int countValidOrHitCells(List<Posn> posns) {
    int count = 0;
    for (Posn posn : posns) {
      try {
        if (isValidCell(posn) || enemyBoard.checkHit(posn)) {
          count++;
        } else {
          break;
        }
      } catch (IndexOutOfBoundsException e) {
        break;
      }

    }
    return count;
  }

  /**
   * Is this cell in bounds, not attacked, and not already in the posns to attack list?
   *
   * @param posn posn to check
   * @return true if all 3 conditions are met
   */
  private boolean isValidCell(Posn posn) {
    boolean outOfBounds = !this.enemyBoard.isValidPosn(posn);
    if (outOfBounds) {
      return false;
    }
    boolean notAttacked = !this.enemyBoard.checkAttacked(posn);
    boolean notInToAttackList = !posnsToAttack.contains(posn);
    boolean notInCurrentVolley = !currentVolley.contains(posn);

    return notAttacked && notInToAttackList && notInCurrentVolley;
  }

  /**
   * checks if all the coordinates in the list of coordinates have a hit ship or are in the volley
   *
   * @param posns list of coordinates to check
   * @return if all the coordinates in the list of coordinates have a hit ship or are in the volley
   */
  private boolean andMapIsHit(List<Posn> posns) {
    for (Posn posn : posns) {
      if (!isHitOrInVolley(posn)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Is this cell a hit ship or in the volley?
   *
   * @param posn coordinate to check
   * @return true if any condition is met
   */
  private boolean isHitOrInVolley(Posn posn) {
    boolean outOfBounds = !this.enemyBoard.isValidPosn(posn);
    if (outOfBounds) {
      return false;
    }

    return this.enemyBoard.checkHit(posn) || this.currentVolley.contains(posn);
  }


  /**
   * Generate a list of shots to fire when no ship locations are known
   * attempts to efficiently find enemy ships by calculating the
   * position with the most possible ship placements
   * If no cells available from the predetermined "every 3" pattern, will fire near current hits
   * as this indicates that there are no more "new" ships to find
   */
  private void shipFinder() {
    int shotCount = shotsToFire;
    for (int i = 0; i < shotCount; i++) {
      Posn shot = this.singleShipFinder();
      this.currentVolley.add(shot);
      this.lastFinderPosns.add(shot);
      shotsToFire--;
    }
    System.out.println("Fired " + shotCount + " shots to find ships!");
  }

  /**
   * Generate a single shot to fire when no ship locations are known
   * attempts to efficiently find enemy ships by calculating the
   * position with the most possible ship placements
   * If no cells available from the predetermined "every 3" pattern, will fire near current hits
   * as this indicates that there are no more "new" ships to find
   *
   * @return single shot to add to the volley
   */
  private Posn singleShipFinder() {
    int height = this.enemyBoard.getHeight();
    int width = this.enemyBoard.getWidth();
    int maxProbability = 0;
    Posn maxProbabillityPosn = null;

    for (int x = width - 1; x >= 0; x--) {
      int displacement = x % 3;

      for (int y = height - 1 - displacement; y >= 0; y = y - 3) {
        Posn targetPosn = new Posn(x, y);
        int probability = calculateShipPermutations(targetPosn);
        if (isValidCell(targetPosn) && probability > maxProbability) {
          maxProbability = probability;
          maxProbabillityPosn = targetPosn;
        }
      }
    }

    if (maxProbabillityPosn != null) {
      return maxProbabillityPosn;
    } else {
      return this.fireNextToShips();
    }
  }

  /**
   * Fire at available cells near current hits
   *
   * @return position adjacent to a hit
   */
  private Posn fireNextToShips() {
    int width = this.enemyBoard.getWidth();
    int height = this.enemyBoard.getHeight();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Posn posn = new Posn(x, y);
        if (!isValidCell(posn)) {
          continue;
        }

        for (Posn adjacentPosn : getAdjacentPosns(posn)) {
          if (!enemyBoard.isValidPosn(adjacentPosn)) {
            continue;
          }
          if (enemyBoard.checkHit(adjacentPosn)) {
            return posn;
          }
        }
      }
    }
    return fireOneRandomShot();
  }

  /**
   * returns all adjacent coordinates to the given coordinate
   *
   * @param posn the coordinate to get its surroundings
   * @return the surrounding coordinates of the given coordinate
   */
  List<Posn> getAdjacentPosns(Posn posn) {
    List<Posn> output = new ArrayList<>();

    List<Posn> offsets = new ArrayList<>(
        Arrays.asList(new Posn(0, -1), new Posn(0, 1), new Posn(1, 0), new Posn(-1, 0)));

    for (Posn offset : offsets) {
      Posn adjacentPosn = posn.add(offset);
      output.add(adjacentPosn);
    }
    return output;
  }


  /**
   * Generate a random non-attacked posn to be fired at
   *
   * @return Posn to be fired
   */
  private Posn fireOneRandomShot() {
    int width = this.enemyBoard.getWidth();
    int height = this.enemyBoard.getHeight();
    boolean successful = false;
    Posn posn = null;
    while (!successful) {
      posn = Posn.randomPosn(width, height);
      if (isValidCell(posn)) {

        successful = this.enemyBoard.attack(posn);
      }
    }
    return posn;
  }


  /**
   * Calculate the total number of possible permutations
   *
   * @param posn target posn
   * @return total number of possible ways to place ships on the target posn
   */
  private int calculateShipPermutations(Posn posn) {
    int height = this.enemyBoard.getHeight();
    int width = this.enemyBoard.getWidth();
    int availableAbove = countValidOrHitCells(posn.offsetY(-height));
    int availableBelow = countValidOrHitCells(posn.offsetY(height));
    int availableLeft = countValidOrHitCells(posn.offsetX(-width));
    int availableRight = countValidOrHitCells(posn.offsetX(width));

    int totalPermutations = 0;

    for (ShipType shipType : this.specifications.keySet()) {
      int shipSize = shipType.size();
      int shipNum = this.specifications.get(shipType);
      int verticalPermutations =
          calculatePartialPermutations(shipSize, availableAbove, availableBelow);
      int horizontalPermutations =
          calculatePartialPermutations(shipSize, availableLeft, availableRight);
      totalPermutations += (verticalPermutations + horizontalPermutations) * shipNum;
    }

    return totalPermutations;
  }

  /**
   * Helper function for calculating ship permutations
   *
   * @param shipSize   ship size to analyze
   * @param availableA space available above/right of the target posn
   * @param availableB space available below/left of the target posn
   * @return the number of permutations in a single direction
   */
  private int calculatePartialPermutations(int shipSize, int availableA, int availableB) {
    int partialPermutations = 0;

    for (int i = 0; i <= shipSize; i++) {
      partialPermutations +=
          calculateCombinations(i, availableA) * calculateCombinations(shipSize - i, availableB);
    }

    return partialPermutations;
  }

  /**
   * Calculate a combination operation
   *
   * @param k parameter for combination operation
   * @param n parameter for combination operation
   * @return integer result
   */
  private int calculateCombinations(int k, int n) {
    int combinations = 1;

    if (k == 0 || k == n) {
      return combinations;
    }

    for (int i = 1; i <= k; i++) {
      combinations *= (n - i + 1) / i;
    }

    return combinations;
  }
}

