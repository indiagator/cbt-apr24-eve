package com.cbt.cbtapr24eve;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
public class AppController
{
    private final LogisticrfqRepository logisticrfqRepository;
    CredentialRepository credentialRepository;
    UserdetailRepository userdetailRepository;
    WalletRepository walletRepository;
    UsernamewalletlinkRepository usernamewalletlinkRepository;
    UsertypelinkRepository usertypelinkRepository;
    ProductRepository productRepository;
    ProductofferRepository productofferRepository;
    ProductofferstatusRepository productofferstatusRepository;
    OrderRepository orderRepository;
    OrderstatusRepository orderstatusRepository;
    OfferOrderViewService offerOrderViewService;
    PaymentRepository paymentRepository;
    PaymentwalletlinkRepository paymentwalletlinkRepository;
    NegomessageRepository negomessageRepository;
    private final PortRepository portRepository;

    AppController(CredentialRepository credentialRepository,
                  UserdetailRepository userdetailRepository,
                  WalletRepository walletRepository,
                  UsernamewalletlinkRepository usernamewalletlinkRepository,
                  UsertypelinkRepository usertypelinkRepository,
                  ProductRepository productRepository,
                  ProductofferRepository productofferRepository,
                  ProductofferstatusRepository productofferstatusRepository,
                  OrderRepository orderRepository,
                  OrderstatusRepository orderstatusRepository,
                  OfferOrderViewService offerOrderViewService,
                  PaymentRepository paymentRepository,
                  PaymentwalletlinkRepository paymentwalletlinkRepository,
                  NegomessageRepository negomessageRepository, LogisticrfqRepository logisticrfqRepository,
                  PortRepository portRepository)
    {
        this.credentialRepository = credentialRepository;
        this.userdetailRepository = userdetailRepository;
        this.walletRepository = walletRepository;
        this.usernamewalletlinkRepository = usernamewalletlinkRepository;
        this.usertypelinkRepository = usertypelinkRepository;
        this.productRepository = productRepository;
        this.productofferRepository = productofferRepository;
        this.productofferstatusRepository = productofferstatusRepository;
        this.orderRepository = orderRepository;
        this.orderstatusRepository = orderstatusRepository;
        this.offerOrderViewService = offerOrderViewService;
        this.paymentRepository = paymentRepository;
        this.paymentwalletlinkRepository = paymentwalletlinkRepository;
        this.negomessageRepository = negomessageRepository;
        this.logisticrfqRepository = logisticrfqRepository;
        this.portRepository = portRepository;
    }

    @GetMapping("get/req")
    public List<String> testTomcat(@RequestParam String data , HttpServletRequest request)
    {
        return Collections.list(request.getHeaderNames());
    }


    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody Credential credential )
    {
        //persist it to db
        credentialRepository.save(credential);
        return ResponseEntity.ok("New User Signed Up");
    }

    //@PostMapping("login")
    //public ResponseEntity<String> login(@RequestBody Credential credential)
    //{
        //check if the user exists in db
        //if yes, return ok
        //else return unauthorized
        //return ResponseEntity.ok("User Logged In");
    //}

    @PostMapping("save/userdetails")
    public ResponseEntity<Userdetail> saveUserDetails(@RequestBody Userdetail userdetail)
    {
            userdetailRepository.save(userdetail);

            Wallet wallet = new Wallet();
            wallet.setWalletid(String.valueOf(UUID.randomUUID()));
            wallet.setBalance(5000000);

            walletRepository.save(wallet);

            Usernamewalletlink usernamewalletlink = new Usernamewalletlink();
            usernamewalletlink.setWalletid(wallet.getWalletid());
            usernamewalletlink.setUsername(userdetail.getUsername());

            usernamewalletlinkRepository.save(usernamewalletlink);

            return ResponseEntity.ok(userdetail);
    }

    @PostMapping("save/user/type/{username}/{usertype}")
    public ResponseEntity<Usertypelink>  saveUserType(@PathVariable String username,
                                                      @PathVariable String usertype,
                                                      Usertypelink usertypelink)
    {
        usertypelink.setLinkid(String.valueOf(UUID.randomUUID()));
        usertypelink.setUsername(username);
        usertypelink.setType(usertype);

        usertypelinkRepository.save(usertypelink);

        return ResponseEntity.ok(usertypelink);
    }

    @GetMapping("get/all/products")
    public ResponseEntity<List<Product>> getAllProducts()
    {
       return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("create/offer")
    public ResponseEntity<Productoffer> createOrder(@RequestBody Productoffer productoffer,
                                                    Productofferstatus productofferstatus)
    {
        productoffer.setId(String.valueOf(UUID.randomUUID()));
        productofferRepository.save(productoffer);

        productofferstatus.setId(String.valueOf(UUID.randomUUID()));
        productofferstatus.setOfferid(productoffer.getId());
        productofferstatus.setStatus("OPEN");
        productofferstatusRepository.save(productofferstatus);

        return ResponseEntity.ok(productoffer);
    }

    @GetMapping("get/user/details/{username}")
    public ResponseEntity<Userdetail> getUserDetails(@PathVariable String username)
    {
        return ResponseEntity.ok(userdetailRepository.findByUsername(username));
    }

    @PostMapping("create/order")
    @Transactional
    public ResponseEntity<Order> createOrder(@RequestBody Order order,
                                             Orderstatus orderstatus)
    {
        order.setOrderid(String.valueOf(UUID.randomUUID()));
        orderRepository.save(order);

        orderstatus.setId(String.valueOf(UUID.randomUUID()));
        orderstatus.setOrderid(order.getOrderid());
        orderstatus.setStatus("OPEN");
        orderstatusRepository.save(orderstatus);

        return ResponseEntity.ok(order);
    }

    @GetMapping("get/offers/open")
    public ResponseEntity<List<Productoffer>> getAllOpenOffers()
    {
        List<Productofferstatus> productofferstatuses =   productofferstatusRepository.findByStatus("OPEN");
        List<Productoffer> offers =  productofferstatuses.stream().map(status -> productofferRepository.findById(status.getOfferid()).get()).collect(Collectors.toList());
        return ResponseEntity.ok(offers);
    }

    @GetMapping("get/orders/{sellername}")
    public ResponseEntity<List<OfferOrderView>> getOfferOrderViews(@PathVariable String sellername)
    {
        List<Productoffer> productoffers =  productofferRepository.findBySellername(sellername);productofferRepository.findBySellername(sellername);
        List<OfferOrderView> offerOrderViews = productoffers.stream().filter(productoffer -> offerOrderViewService.createOfferOrderView(productoffer.getId(),new OfferOrderView()).isPresent()).map(productoffer -> offerOrderViewService.createOfferOrderView(productoffer.getId(),new OfferOrderView()).get()).collect(Collectors.toList());
        return ResponseEntity.ok(offerOrderViews);
    }

    @PostMapping("accept/order/{orderid}")
    @Transactional
    public ResponseEntity<String> acceptOrder(@PathVariable String orderid,
                                              Payment payment,
                                              Paymentwalletlink paymentwalletlink)
    {

        if(orderstatusRepository.findByOrderid(orderid).isPresent())
        {

            //UPDATE STATUS OF OFFER TO PROCESSING
            String offerid  =orderRepository.findById(orderid).get().getOfferid();
            productofferstatusRepository.updateStatusByOfferid("PROCESSING",offerid);

            //UPDATE STATUS OF ORDER TO ACCEPTED/REJECTED
            orderstatusRepository.updateStatusByOrderid("ACCEPTED", orderid);
            OfferOrderView ordersView = offerOrderViewService.createOfferOrderView(offerid, new OfferOrderView()).get();
            List<Order> orders = ordersView.getOrders();
            List<Order> otherOrders = orders.stream().filter(order -> !(order.getOrderid().equals(orderid))).collect(Collectors.toList());
            if(!otherOrders.isEmpty())
            {
                otherOrders.stream().forEach(order -> orderstatusRepository.updateStatusByOrderid("REJECTED", order.getOrderid()));
            }

            //CREATE A PAYMENT WITH STATUS "DUE"
            String buyername = orderRepository.findById(orderid).get().getBuyername();
            String sellername = productofferRepository.findById(offerid).get().getSellername();

            payment.setId(String.valueOf(UUID.randomUUID()));
            payment.setOrderid(orderid);
            payment.setOfferid(offerid);
            payment.setStatus("DUE");

            paymentwalletlink.setLinkid(String.valueOf(UUID.randomUUID()));
            paymentwalletlink.setPayerwallet(usernamewalletlinkRepository.findById(buyername).get().getWalletid());
            paymentwalletlink.setPayeewallet(usernamewalletlinkRepository.findById(sellername).get().getWalletid());
            paymentwalletlink.setEscrowwallet(usernamewalletlinkRepository.findById("indiagator").get().getWalletid());
            paymentwalletlink.setAmount(orderRepository.findById(orderid).get().getBid());
            paymentwalletlink.setPaymentrefid(payment.getId());
            paymentwalletlink.setPaymenttype("ORDER");

            payment.setPaymentwalletlink(paymentwalletlink.getLinkid());

            paymentwalletlinkRepository.save(paymentwalletlink);
            paymentRepository.save(payment);
        }

        return ResponseEntity.ok("Order Accepted");
    }


    @PostMapping("send/message")
    public ResponseEntity<Negomessage> sendMessage(@RequestBody Negomessage negomessage)
    {
        negomessage.setId(String.valueOf(UUID.randomUUID()));
        negomessage.setTime(Instant.now());
        negomessageRepository.save(negomessage);
        return ResponseEntity.ok(negomessage);
    }

    @PostMapping("make/payment/{orderid}")
    @Transactional
    public ResponseEntity<Paymentxn> makePayment(@PathVariable String orderid, Paymentxn paymentxn)
    {

        if(paymentRepository.findByOrderid(orderid).isPresent())
        {
            Payment payment = paymentRepository.findByOrderid(orderid).get();
            Paymentwalletlink paymentwalletlink = paymentwalletlinkRepository.findById(payment.getPaymentwalletlink()).get();

            //DEBIT THE AMOUNT FROM BUYER WALLET
            Wallet buyerwallet = walletRepository.findById(paymentwalletlink.getPayerwallet()).get();
            buyerwallet.setBalance(buyerwallet.getBalance() - paymentwalletlink.getAmount());

            //CREDIT THE AMOUNT TO ESCROW WALLET
            Wallet escrowwallet = walletRepository.findById(paymentwalletlink.getEscrowwallet()).get();
            escrowwallet.setBalance(escrowwallet.getBalance() + paymentwalletlink.getAmount());

            paymentxn.setTxnid(String.valueOf(UUID.randomUUID()));
            paymentxn.setPymntrefid(orderid);
            paymentxn.setPaymenttype("ORDER");
            paymentxn.setTime(Instant.now());
            paymentxn.setAmount(paymentwalletlink.getAmount());
            paymentxn.setPayerwallet(paymentwalletlink.getPayerwallet());
            paymentxn.setPayeewallet(paymentwalletlink.getEscrowwallet());

            return ResponseEntity.ok(paymentxn);
        }
        else
        {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("get/conversation/{orderid}")
    public ResponseEntity<List<String>> showConversation(@PathVariable String orderid)
    {
         List<String> messages =  negomessageRepository.findByNegorefid(orderid).stream().
                 map(negomessage -> negomessage.toString()).collect(Collectors.toList());

         return ResponseEntity.ok(messages);
    }

    @PostMapping("create/log/rfq")
    public ResponseEntity<Logisticrfq> createLogRFQ(@RequestBody Logisticrfq logisticrfq)
    {
        logisticrfq.setRfqid(String.valueOf(UUID.randomUUID()));
        logisticrfq.setStatus("OPEN");
        logisticrfqRepository.save(logisticrfq);

        return ResponseEntity.ok(logisticrfq);
    }

    @GetMapping("get/ports")
    public ResponseEntity<List<Port>> getPorts()
    {
        return ResponseEntity.ok(portRepository.findAll());
    }



}
