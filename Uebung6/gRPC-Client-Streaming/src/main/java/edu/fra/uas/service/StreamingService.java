package edu.fra.uas.service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;

// compiled classes (generated by the protoc compiler) are imported
import edu.fra.uas.grpc.StockQuoteServiceGrpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import edu.fra.uas.grpc.StockQuoteOuterClass.Stock;
import edu.fra.uas.grpc.StockQuoteOuterClass.StockQuote;

@Service
public class StreamingService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(StreamingService.class);

    @GrpcClient("streaming-service-server")
    private StockQuoteServiceGrpc.StockQuoteServiceBlockingStub stockQuoteServiceStub;

    @GrpcClient("streaming-service-server")
    private StockQuoteServiceGrpc.StockQuoteServiceStub nonBlockingStub;

    public void serverSideStreamingListStockQuotes() {
        log.info("serverSideStreamingListStockQuotes called");
        
        Stock stock = Stock.newBuilder().setSymbol("Au").build();
        Iterator<StockQuote> stockQuoteIterator;
        try {
            log.info("--> send Request - symbol {}", stock.getSymbol());
            stockQuoteIterator = stockQuoteServiceStub.serverSideStreamingGetListStockQuotes(stock);
            while (stockQuoteIterator.hasNext()) {
                StockQuote stockQuote = stockQuoteIterator.next();
                log.info("<-- received Response - stockQuote: \n{}", stockQuote.toString());
            }
        } catch(StatusRuntimeException  e) {
            log.error("RPC failed: {}", e.getStatus());
        }
    }

    public void clientSideStreamingStatisticsOfStocks() throws InterruptedException {
        log.info("clientSideStreamingStatisticsOfStocks called");
        final CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<StockQuote> responseObserver = new StreamObserver<StockQuote>() {
            @Override
            public void onNext(StockQuote stockQuote) {
                log.info("<-- received Response - stockQuote statistics: {} Price: {}", stockQuote.getSymbol(), stockQuote.getPrice());
            }

            @Override
            public void onError(Throwable t) {
                log.error("clientSideStreamingStatisticsOfStocks failed: {}", Status.fromThrowable(t));
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("clientSideStreamingStatisticsOfStocks completed");
                finishLatch.countDown();
            }
        };

        StreamObserver<Stock> requestObserver = nonBlockingStub.clientSideStreamingGetStatisticsOfStocks(responseObserver);
        List<Stock> stocks = List.of(
                Stock.newBuilder().setSymbol("Au").build(),
                Stock.newBuilder().setSymbol("Ag").build(),
                Stock.newBuilder().setSymbol("Pt").build(),
                Stock.newBuilder().setSymbol("Pd").build(),
                Stock.newBuilder().setSymbol("Cu").build()
        );
        try {
            for (Stock stock: stocks) {
                log.info("--> send Request - symbol {}", stock.getSymbol());
                requestObserver.onNext(stock);
                if (finishLatch.getCount() == 0) {
                    // RPC completed or errored before we finished sending.
                    // Sending further requests won't error, but they will just be thrown away.
                    return;
                }
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            log.error("clientSideStreamingStatisticsOfStocks can not finish within 1 minute");
        }
    }

    public void bidirectionalStreamingListsStockQuotes() throws InterruptedException {
        log.info("bidirectionalStreamingListsStockQuotes called");
        final CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<StockQuote> requestObserver = new StreamObserver<StockQuote>() {
            @Override
            public void onNext(StockQuote stockQuote) {
                log.info("<-- received Response - stockQuote: \n{}", stockQuote.toString());
            }

            @Override
            public void onError(Throwable t) {
                log.error("bidirectionalStreamingListsStockQuotes failed: {}", Status.fromThrowable(t));
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("bidirectionalStreamingListsStockQuotes completed");
                finishLatch.countDown();
            }
        };

        StreamObserver<Stock> responseObserver = nonBlockingStub.bidirectionalStreamingGetListsStockQuotes(requestObserver);
        List<Stock> stocks = List.of(
                Stock.newBuilder().setSymbol("Au").build(),
                Stock.newBuilder().setSymbol("Ag").build(),
                Stock.newBuilder().setSymbol("Pt").build(),
                Stock.newBuilder().setSymbol("Pd").build(),
                Stock.newBuilder().setSymbol("Cu").build()
        );
        try {
            for (Stock stock: stocks) {
                log.info("--> send Request - symbol {}", stock.getSymbol());
                responseObserver.onNext(stock);
                Thread.sleep(1000);
                if (finishLatch.getCount() == 0) {
                    return;
                }
            }
        } catch (RuntimeException e) {
            responseObserver.onError(e);
            throw e;
        }
        responseObserver.onCompleted();
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            log.error("bidirectionalStreamingListsStockQuotes can not finish within 1 minute");
        }
    }

}