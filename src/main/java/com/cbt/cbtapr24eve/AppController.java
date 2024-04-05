package com.cbt.cbtapr24eve;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
public class AppController
{
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


    AppController( CredentialRepository credentialRepository,
                   UserdetailRepository userdetailRepository,
                   WalletRepository walletRepository,
                   UsernamewalletlinkRepository usernamewalletlinkRepository,
                   UsertypelinkRepository usertypelinkRepository,
                   ProductRepository productRepository,
                   ProductofferRepository productofferRepository,
                   ProductofferstatusRepository productofferstatusRepository,
                   OrderRepository orderRepository,
                   OrderstatusRepository orderstatusRepository,
                   OfferOrderViewService offerOrderViewService)
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





}
