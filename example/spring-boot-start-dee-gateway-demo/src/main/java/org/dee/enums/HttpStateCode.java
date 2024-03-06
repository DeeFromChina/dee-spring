package org.dee.enums;

/**
 * Http返回码
 */
public enum HttpStateCode {

    /**
     * 100 Continue 继续。客户端应继续其请求
     */
    CONTINUE(100,"Continue"),
    /**
     * 101 Switching Protocols 切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到 HTTP 的新版本协议
     */
    SWITCHING_PROTOCOLS(101,"Switching Protocols"),
    /**
     * 102 Processing（WebDAV；RFC 2518） WebDAV请求可能包含许多涉及文件操作的子请求，需要很长时间才能完成请求。该代码表示服务器已经收到并正在处理请求，但无响应可用。[6]这样可以防止客户端超时，并假设请求丢失。
     */
    PROCESSING(102,"Processing"),
    /**
     * 200 OK 请求成功。一般用于 GET 与 POST 请求
     */
    OK(200,"OK"),
    /**
     * 201 Created 已创建。成功请求并创建了新的资源
     */
    CREATED(201,"Created"),
    /**
     * 202 Accepted 已接受。已经接受请求，但未处理完成
     */
    ACCEPTED(202,"Accepted"),
    /**
     * 203 Non-Authoritative Information 非授权信息。请求成功。但返回的 meta 信息不在原始的服务器，而是一个副本
     */
    NON_AUTHORITATIVE_INFORMATION(203,"Non-Authoritative Information"),
    /**
     * 204 No Content 无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，
     * 可确保浏览器继续显示当前文档205 Reset Content 重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文 档视图。可通过此返回码清除浏览器的表单域
     */
    NO_CONTENT(204,"No Content"),
    /**
     * 205 Reset Content 服务器成功处理了请求，但没有返回任何内容。与204响应不同，此响应要求请求者重置文档视图。
     */
    RESET_CONTENT(205,"Reset Content"),
    /**
     * 206 Partial Content 部分内容。服务器成功处理了部分 GET 请求
     */
    PARTIAL_CONTENT(206,"Partial Content"),
    /**
     * 207 Multi-Status（WebDAV；RFC 4918） 代表之后的消息体将是一个XML消息，并且可能依照之前子请求数量的不同，包含一系列独立的响应代码。
     */
    MULTI_STATUS(207,"Multi-Status"),
    /**
     * 208 Already Reported （WebDAV；RFC 5842） DAV绑定的成员已经在（多状态）响应之前的部分被列举，且未被再次包含。
     */
    ALREADY_REPORTED(208,"Already Reported"),
    /**
     * 226 IM Used （RFC 3229） 服务器已经满足了对资源的请求，对实体请求的一个或多个实体操作的结果表示。
     */
    IM_USED(226,"IM Used"),
    /**
     * 300 Multiple Choices 被请求的资源有一系列可供选择的回馈信息，每个都有自己特定的地址和浏览器驱动的商议信息。
     * 用户或浏览器能够自行选择一个首选的地址进行重定向。
     * 除非这是一个HEAD请求，否则该响应应当包括一个资源特性及地址的列表的实体，以便用户或浏览器从中选择最合适的重定向地址。
     * 这个实体的格式由Content-Type定义的格式所决定。浏览器可能根据响应的格式以及浏览器自身能力，自动作出最合适的选择。
     * 当然，RFC 2616规范并没有规定这样的自动选择该如何进行。 如果服务器本身已经有了首选的回馈选择，那么在Location中应当指明这个回馈的URI；
     * 浏览器可能会将这个Location值作为自动重定向的地址。此外，除非额外指定，否则这个响应也是可缓存的。
     */
    MULTIPLE_CHOICES(300,"Multiple Choices"),
    /**
     * 301 Moved Permanently 永久移动。请求的资源已被永久的移动到新 URI，返回信息会
     * 包括新的 URL，浏览器会自动定向到新 URL。今后任何新的请求都应使用新的 URL 代替。
     */
    MOVED_PERMANENTLY(301,"Moved Permanently"),
    /**
     * 302 Found 临时重定向,但资源只是临时被移动,客户端应继续使用原本的URL。
     */
    FOUND(302,"Found"),
    /**
     * 303 See Other 对应当前请求的响应可以在另一个URI上被找到，当响应于POST（或PUT / DELETE）接收到响应时，客户端应该假定服务器已经收到数据，并且应该使用单独的GET消息发出重定向。
     * [23]这个方法的存在主要是为了允许由脚本激活的POST请求输出重定向到一个新的资源。这个新的URI不是原始资源的替代引用。同时，303响应禁止被缓存。
     * 当然，第二个请求（重定向）可能被缓存。
     * 新的URI应当在响应的Location域中返回。除非这是一个HEAD请求，否则响应的实体中应当包含指向新的URI的超链接及简短说明。
     * 注意：许多HTTP/1.1版以前的浏览器不能正确理解303状态。如果需要考虑与这些浏览器之间的互动，302状态码应该可以胜任，因为大多数的浏览器处理302响应时的方式恰恰就是上述规范要求客户端处理303响应时应当做的。
     */
    SEE_OTHER(303,"See Other"),
    /**
     * 304 Not Modified 未修改。所请求的资源未修改，服务器返回此状态码时，不会返回
     * 任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
     */
    NOT_MODIFIED(304,"Not Modified"),
    /**
     * 305 Use Proxy 被请求的资源必须通过指定的代理才能被访问。
     * Location域中将给出指定的代理所在的URI信息，接收者需要重复发送一个单独的请求，通过这个代理才能访问相应资源。
     * 只有原始服务器才能创建305响应。
     * 许多HTTP客户端（像是Mozilla和Internet Explorer）都没有正确处理这种状态代码的响应，主要是出于安全考虑。
     * 注意：RFC 2068中没有明确305响应是为了重定向一个单独的请求，而且只能被原始服务器建立。忽视这些限制可能导致严重的安全后果。
     */
    USE_PROXY(305,"Use Proxy"),
    /**
     * 306 Switch Proxy 在最新版的规范中，306状态码已经不再被使用。最初是指“后续请求应使用指定的代理”。
     */
    SWITCH_PROXY(306,"Switch Proxy"),
    /**
     * 307 Temporary Redirect 在这种情况下，请求应该与另一个URI重复，但后续的请求应仍使用原始的URI。
     * 与302相反，当重新发出原始请求时，不允许更改请求方法。 例如，应该使用另一个POST请求来重复POST请求。
     */
    TEMPORARY_REDIRECT(307,"Temporary Redirect"),
    /**
     * 308 Permanent Redirect (RFC 7538) 请求和所有将来的请求应该使用另一个URI重复。
     * 307和308重复302和301的行为，但不允许HTTP方法更改。 例如，将表单提交给永久重定向的资源可能会顺利进行。
     */
    PERMANENT_REDIRECT(308,"Permanent Redirect"),
    /**
     * 400 Bad Request 客户端请求的语法错误，服务器无法理解
     */
    BAD_REQUEST(400,"Bad Request"),
    /**
     * 401 Unauthorized 请求要求用户的身份认证
     */
    UNAUTHORIZED(401,"Unauthorized"),
    /**
     * 402 Payment Required 保留，将来使用
     */
    PAYMENT(402,"Payment"),
    /**
     * 403 Forbidden 服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    FORBIDDEN(403,"Forbidden"),
    /**
     * 404 Not Found 服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
     */
    NOT_FOUND(404,"Not Found"),
    /**
     * 405 Method Not Allowed 客户端请求中的方法被禁止
     */
    METHOD_NOT_ALLOWED(405,"Method Not Allowed"),
    /**
     * 406 Not Acceptable 服务器无法根据客户端请求的内容特性完成请求
     */
    NOT_ACCEPTABLE(406,"Not Acceptable"),
    /**
     * 407 Proxy Authentication Required 请求要求代理的身份认证，与 401 类似，但请求者应当使用代理进行授权
     */
    PROXY_AUTHENTICATION_REQUIRED(407,"Proxy Authentication Required"),
    /**
     * 408 Request Time-out 服务器等待客户端发送的请求时间过长，超时
     */
    REQUEST_TIME_OUT(408,"Request Time-out"),
    /**
     * 409 Conflict 表示因为请求存在冲突无法处理该请求，例如多个同步更新之间的编辑冲突。
     */
    CONFLICT(408,"Conflict"),
    /**
     * 410 Gone 表示所请求的资源不再可用，将不再可用。当资源被有意地删除并且资源应被清除时，应该使用这个。
     * 在收到410状态码后，用户应停止再次请求资源。但大多数服务端不会使用此状态码，而是直接使用404状态码。
     */
    GONE(410,"Gone"),
    /**
     * 411 Length Required 服务器拒绝在没有定义Content-Length头的情况下接受请求。在添加了表明请求消息体长度的有效Content-Length头之后，客户端可以再次提交该请求。
     */
    LENGTH_REQUIRED(411,"Length Required"),
    /**
     * 412 Precondition Failed（RFC 7232） 服务器在验证在请求的头字段中给出先决条件时，没能满足其中的一个或多个。
     * 这个状态码允许客户端在获取资源时在请求的元信息（请求头字段数据）中设置先决条件，以此避免该请求方法被应用到其希望的内容以外的资源上。
     */
    PRECONDITION_FAILED(412,"Precondition Failed"),
    /**
     * 413 Request Entity Too Large（RFC 7231） 前称“Request Entity Too Large”，表示服务器拒绝处理当前请求，因为该请求提交的实体数据大小超过了服务器愿意或者能够处理的范围。
     * 此种情况下，服务器可以关闭连接以免客户端继续发送此请求。
     * 如果这个状况是临时的，服务器应当返回一个Retry-After的响应头，以告知客户端可以在多少时间以后重新尝试。
     */
    REQUEST_ENTITY_TOO_LARGE(413,"Request Entity Too Large"),
    /**
     * 414 Request-URI Too Long（RFC 7231） 前称“Request-URI Too Long”，表示请求的URI长度超过了服务器能够解释的长度，因此服务器拒绝对该请求提供服务。
     * 通常将太多数据的结果编码为GET请求的查询字符串，在这种情况下，应将其转换为POST请求。
     * 这比较少见，通常的情况包括： 本应使用POST方法的表单提交变成了GET方法，导致查询字符串过长。
     * 重定向URI“黑洞”，例如每次重定向把旧的URI作为新的URI的一部分，导致在若干次重定向后URI超长。
     * 客户端正在尝试利用某些服务器中存在的安全漏洞攻击服务器。
     * 这类服务器使用固定长度的缓冲读取或操作请求的URI，当GET后的参数超过某个数值后，可能会产生缓冲区溢出，导致任意代码被执行。
     * 没有此类漏洞的服务器，应当返回414状态码。
     */
    REQUEST_URI_TOO_LONG(414,"Request-URI Too Long"),
    /**
     * 415 Unsupported Media Type 对于当前请求的方法和所请求的资源，请求中提交的互联网媒体类型并不是服务器中所支持的格式，因此请求被拒绝。
     * 例如，客户端将图像上传格式为svg，但服务器要求图像使用上传格式为jpg。
     */
    UNSUPPORTED_MEDIA_TYPE(415,"Unsupported Media Type"),
    /**
     * 416 Requested Range Not Satisfiable（RFC 7233） 前称“Requested Range Not Satisfiable”。
     * 客户端已经要求文件的一部分（Byte serving），但服务器不能提供该部分。例如，如果客户端要求文件的一部分超出文件尾端。
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416,"Requested Range Not Satisfiable"),
    /**
     * 417 Expectation Failed 在请求头Expect中指定的预期内容无法被服务器满足，或者这个服务器是一个代理服显的证据证明在当前路由的下一个节点上，Expect的内容无法被满足。
     */
    EXPECTATION_FAILED(417,"Expectation Failed"),
    /**
     * 418 I'm a teapot（RFC 2324） 本操作码是在1998年作为IETF的传统愚人节笑话, 在RFC 2324超文本咖啡壶控制协议'中定义的，并不需要在真实的HTTP服务器中定义。
     * 当一个控制茶壶的HTCPCP收到BREW或POST指令要求其煮咖啡时应当回传此错误。这个HTTP状态码在某些网站（包括Google.com）与项目（如Node.js、ASP.NET和Go语言）中用作彩蛋。
     */
    I_AM_A_TEAPOT(418,"I'm a teapot"),
    /**
     * 420 Enhance Your Caim Twitter Search与Trends API在客户端被限速的情况下返回。
     */
    ENHANCE_YOUR_CAIM(420,"Enhance Your Caim"),
    /**
     * 421 Misdirected Request （RFC 7540） 该请求针对的是无法产生响应的服务器（例如因为连接重用）。
     */
    MISDIRECTED_REQUEST(421,"Misdirected Request"),
    /**
     * 422 Unprocessable Entity（WebDAV；RFC 4918 ） 请求格式正确，但是由于含有语义错误，无法响应。
     */
    UNPROCESSABLE_ENTITY(422,"Unprocessable Entity"),
    /**
     * 423 Locked（WebDAV；RFC 4918） 当前资源被锁定。
     */
    LOCKED(423,"Locked"),
    /**
     * 424 Failed Dependency（WebDAV；RFC 4918） 由于之前的某个请求发生的错误，导致当前请求失败，例如PROPPATCH。
     */
    FAILED_DEPENDENCY(424,"Failed Dependency"),
    /**
     * 425 Unordered Collection 在WebDAV Advanced Collections Protocol中定义，但Web Distributed Authoring and Versioning (WebDAV) Ordered Collections Protocol中并不存在。
     */
    UNORDERED_COLLECTION(425,"Unordered Collection"),
    /**
     * 426 Upgrade Required（RFC 2817） 客户端应当切换到TLS/1.0，并在HTTP/1.1 Upgrade header中给出。
     */
    UPGRADE_REQUIRED(426,"Upgrade Required"),
    /**
     * 428 Precondition Required (RFC 6585) 原服务器要求该请求满足一定条件。这是为了防止“‘未更新’问题，
     * 即客户端读取（GET）一个资源的状态，更改它，并将它写（PUT）回服务器，但这期间第三方已经在服务器上更改了该资源的状态，因此导致了冲突。”
     */
    PRECONDITION_REQUIRED(428,"Precondition Required"),
    /**
     * 429 Too Many Requests （RFC 6585） 用户在给定的时间内发送了太多的请求。旨在用于网络限速。
     */
    TOO_MANY_REQUESTS(429,"Too Many Requests"),
    /**
     * 431 Request Header Fields Too Large （RFC 6585） 服务器不愿处理请求，因为一个或多个头字段过大。
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE(431,"Request_Header_Fields_Too_Large"),
    /**
     * 444 No Response Nginx上HTTP服务器扩展。服务器不向客户端返回任何信息，并关闭连接（有助于阻止恶意软件）。
     */
            NO_RESPONSE(444,"No Response"),
    /**
     * 450 Blocked by Windows Parental Controls 这是一个由Windows家庭控制（Microsoft）HTTP阻止的450状态代码的示例，用于信息和测试。
     */
    BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS(450,"Blocked by Windows Parental Controls"),
    /**
     * 451 Unavailable For Legal Reasons 该访问因法律的要求而被拒绝，由IETF在2015核准后新增加。
     */
    UNAVAILABLE_FOR_LEGAL_REASONS(451,"Unavailable For Legal Reasons"),
    /**
     * 494 Request Header Too Large 在错误代码431提出之前Nginx上使用的扩展HTTP代码。
     */
    REQUEST_HEADER_TOO_LARGE(494,"Request Header Too Large"),
    /**
     * 500 Internal Server Error 该状态码表示服务器在执行请求的时候出现了错误。
     */
    INTERNAL_SERVER_ERROR(500,"Internal Server Error"),
    /**
     * 501 Not Implemented 服务器不支持当前请求所需要的某个功能。当服务器无法识别请求的方法，并且无法支持其对任何资源的请求。（例如，网络服务API的新功能）
     */
    NOT_IMPLEMENTED(501,"Not Implemented"),
    /**
     * 502 Bad Gateway 作为网关或者代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应。
     */
    BAD_GATEWAY(502,"Bad Gateway"),
    /**
     * 503 Service Unavailable 该状态码表示服务器暂时处于超负载状态或正在停机维护，现在无法处理请求。
     */
    SERVICE_UNAVAILABLE(503,"Service Unavailable"),
    /**
     * 504 Gateway Timeout 作为网关或者代理工作的服务器尝试执行请求时，
     * 未能及时从上游服务器（URI标识出的服务器，例如HTTP、FTP、LDAP）或者辅助服务器（例如DNS）收到响应。
     * 注意：某些代理服务器在DNS查询超时时会返回400或者500错误。
     */
    GATEWAY_TIMEOUT(504,"Gateway Timeout"),
    /**
     * 505 HTTP Version Not Supported 服务器不支持，或者拒绝支持在请求中使用的HTTP版本。
     * [63]这暗示着服务器不能或不愿使用与客户端相同的版本。
     * 响应中应当包含一个描述了为何版本不被支持以及服务器支持哪些协议的实体。
     */
    HTTP_VERSION_NOT_SUPPORTED(505,"HTTP Version Not Supported"),
    /**
     * 506 Variant Also Negotiates（RFC 2295） 由《透明内容协商协议》（RFC 2295）扩展，代表服务器存在内部配置错误，
     * 被请求的协商变元资源被配置为在透明内容协商中使用自己，因此在一个协商处理中不是一个合适的重点。
     */
    VARIANT_ALSO_NEGOTIATES(506,"Variant Also Negotiates"),
    /**
     * 507 Insufficient Storage（WebDAV；RFC 4918） 服务器无法存储完成请求所必须的内容。这个状况被认为是临时的。
     */
    INSUFFICIENT_STORAGE(507,"Insufficient Storage"),
    /**
     * 508 Loop Detected （WebDAV；RFC 5842） 服务器在处理请求时陷入死循环。 （可代替 208状态码）
     */
    LOOP_DETECTED(508,"Loop Detected"),
    /**
     * 510 Not Extended（RFC 2774） 获取资源所需要的策略并没有被满足。
     */
    NOT_EXTENDED(510,"Not Extended"),
    /**
     * 511 Network Authentication Required （RFC 6585） 客户端需要进行身份验证才能获得网络访问权限，旨在限制用户群访问特定网络。（例如连接WiFi热点时的强制网络门户）
     */
    NETWORK_AUTHENTICATION_REQUIRED(511,"Network Authentication Required");

    public final Integer key;

    public final String value;

    HttpStateCode(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public boolean is(Integer s) {
        return s != null && this.getKey().equals(s);
    }

    public Integer getKey(){
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public static HttpStateCode getEnum(Integer key) {
        for (HttpStateCode operateType : HttpStateCode.values()) {
            if (key != null && operateType.key.equals(key)) {
                return operateType;
            }
        }
        throw new RuntimeException("Http返回码不存在！");
    }

}
