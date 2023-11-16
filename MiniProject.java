import java.io.BufferedReader;
import java.io.FileReader;

class StockGame extends Thread {
    private static double stockPrice = 100.00;
    private static int availableShares = 100;
    private static int tradeCount = 0;

    private String name;
    private int numShares;
    private String fileName;

    public StockGame(String name, String fileName) 
    {
    
    	this.name = name;
        this.fileName = fileName;
        this.numShares = 0;
    
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                processTrade(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void processTrade(String trade) {
        String[] parts = trade.split(",");
        if (parts.length != 2) {
            System.out.println("Error, invalid input!");
            return;
        }

        String action = parts[0];
        int amount = Integer.parseInt(parts[1]);

        System.out.println(" --------- ");
        System.out.println("Stock Price: " + stockPrice);
        System.out.println("Available Shares: " + availableShares);
        System.out.println("Trade number: " + (tradeCount + 1)); // Incremented to start from 1
        System.out.println("Name: " + name);

        if ("BUY".equals(action)) {
            if (amount > availableShares) {
                System.out.println("Insufficient shares available, cancelling order...");
                return;
            }

            numShares += amount;
            availableShares -= amount;
            stockPrice += (1.5 * amount);
        } else if ("SELL".equals(action)) {
            if (amount > numShares) {
                System.out.println("Insufficient owned shares, cancelling order...");
                return;
            }

            numShares -= amount;
            availableShares += amount;
            stockPrice -= (2.0 * amount);
        } else {
            System.out.println("Error, invalid input!");
            return;
        }

        System.out.println(action + " " + amount + " shares at " + stockPrice + "...");
        tradeCount++;
    }

    public static void main(String[] args) {
        try {
            StockGame[] stockTraders = {
                    new StockGame("Xander", "TraderOneMoves.txt"),
                    new StockGame("Afua", "TraderTwoMoves.txt")
            };

            for (StockGame trader : stockTraders) {
                trader.start();
                trader.join(); 
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}