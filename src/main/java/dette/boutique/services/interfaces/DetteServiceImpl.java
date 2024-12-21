package dette.boutique.services.interfaces;

import java.util.Date;

import dette.boutique.core.service.Service;
import dette.boutique.data.entities.Dette;

public interface DetteServiceImpl extends Service<Dette> {
    Dette findDette(Date date);
}